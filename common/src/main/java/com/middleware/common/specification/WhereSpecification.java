package com.middleware.common.specification;

import com.middleware.common.config.DynamicQueryFilterLogicType;
import com.middleware.common.config.DynamicQueryOperators;
import com.middleware.common.exception.SpecificationException;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.middleware.common.specification.Filter.PATH_DELIMITER;

public class WhereSpecification implements Specification<Object> {
    private static SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private SimpleDateFormat dateFormat;
    private Filter filter;

    public WhereSpecification(Filter filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<Object> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        return getPredicate(filter, root, cb);
    }

    private Predicate getPredicate(Filter filter, Path<Object> root, CriteriaBuilder cb)  {
        if (isInValidFilter(filter))
            return null;
        if (filter.getLogic() == null) {
            //one filter
            Predicate p = doGetPredicate(filter, root, cb);
            return p;
        } else {
            //logic filters
            if (filter.getLogic() == DynamicQueryFilterLogicType.AND) {
                Predicate[] predicates = getPredicateList(filter, root, cb);
                return cb.and(predicates);
            } else if (filter.getLogic() == DynamicQueryFilterLogicType.OR) {
                Predicate[] predicates = getPredicateList(filter, root, cb);
                return cb.or(predicates);
            } else {
                throw new SpecificationException("Unknown filter logic" + filter.getLogic());
            }
        }
    }

    private Predicate[] getPredicateList(Filter filter, Path<Object> root, CriteriaBuilder cb) {
        List<Predicate> predicateList = new LinkedList<>();
        for (Filter f : filter.getFilters()) {
            Predicate predicate = getPredicate(f, root, cb);
            if (predicate != null)
                predicateList.add(predicate);
        }
        return predicateList.toArray(new Predicate[predicateList.size()]);
    }


    private boolean isInValidFilter(Filter filter) {
        return filter == null ||
                (filter.getField() == null && filter.getFilters() == null && filter.getLogic() == null && filter.getValue() == null && filter.getOperator() == null);
    }

    private Predicate doGetPredicate(Filter filter, Path<Object> root, CriteriaBuilder cb) {
        String field = filter.getField();
        Path<Object> path;
        try {
            path = parsePath(root, field);
        } catch (Exception e) {
            throw new SpecificationException("Met problem when parse field path: " + field + ", this path does not exist.");
        }
        DynamicQueryOperators operator = filter.getOperator();
        Object value = filter.getValue();
        try {
            return doGetPredicate(cb, path, operator, value);
        } catch (Exception e) {
            throw new SpecificationException("Unable to filter by: " + String.valueOf(filter) + ", value type:" + value.getClass() + ", operator: " + operator + ", entity type:" + path.getJavaType());
        }
    }

    private Predicate doGetPredicate(CriteriaBuilder cb, Path<Object> path, DynamicQueryOperators operator, Object value) {
        Predicate p = null;

        switch (operator) {
            /*
                Operator for Comparable type
             */
            case EQUAL:
                value = parseValue(path, value);
                p = cb.equal(path, (value));
                break;
            case NOT_EQUAL:
                value = parseValue(path, value);
                p = cb.notEqual(path, (value));
                break;
            /*
                Operator for any type
             */
            case IS_NULL:
                p = cb.isNull(path);
                break;
            case IS_NOT_NULL:
                p = cb.isNotNull(path);
                break;
            /*
                Operator for String type
             */
            case IS_EMPTY:
                p = cb.equal(path,"");
                break;
            case IS_NOT_EMPTY:
                p = cb.notEqual(path,"");
                break;
            case CONTAINS:
                p = cb.like(path.as(String.class), "%" + String.valueOf(value) + "%");
                break;
            case NOT_CONTAINS:
                p = cb.notLike(path.as(String.class), "%" + String.valueOf(value) + "%");
                break;
            case START_WITH:
                p = cb.like(path.as(String.class), String.valueOf(value) + "%");
                break;
            case END_WITH:
                p = cb.like(path.as(String.class), "%" + String.valueOf(value));
                break;
            /*
                Operator for Comparable type;
                does not support Calendar
             */
            case GREATER_THAN:
                value = parseValue(path, value);
                if (value instanceof Date) {
                    p = cb.greaterThan(path.as(Date.class), (Date) (value));
                } else {
                    p = cb.greaterThan(path.as(String.class), (value).toString());
                }
                break;
            case GREATER_THAN_OR_EQUAL:
                value = parseValue(path, value);
                if (value instanceof Date) {
                    p = cb.greaterThanOrEqualTo(path.as(Date.class), (Date) (value));
                } else {
                    p = cb.greaterThanOrEqualTo(path.as(String.class), (value).toString());
                }
                break;
            case LESS_THAN:
                value = parseValue(path, value);
                if (value instanceof Date) {
                    p = cb.lessThan(path.as(Date.class), (Date) (value));
                } else {
                    p = cb.lessThan(path.as(String.class), (value).toString());
                }
                break;
            case LESS_THAN_OR_EQUAL:
                value = parseValue(path, value);
                if (value instanceof Date) {
                    p = cb.lessThanOrEqualTo(path.as(Date.class), (Date) (value));
                } else {
                    p = cb.lessThanOrEqualTo(path.as(String.class), (value).toString());
                }
                break;
            case IN:
                if (assertCollection(value)) {
                    p = path.in((Collection) value);
                }
                break;
            default:
                throw new SpecificationException("unknown operator: " + operator);
        }
        return p;
    }

    private Object parseValue(Path<Object> path, Object value) {
        if (Date.class.isAssignableFrom(path.getJavaType())) {
            try {
                SimpleDateFormat dateFormat = this.dateFormat != null ? this.dateFormat : defaultDateFormat;
                value = dateFormat.parse(value.toString());
            } catch (ParseException e) {
                throw new SpecificationException("Illegal date format: " + value + ", required format is " + dateFormat.toPattern());
            }
        }
        return value;
    }

    private boolean assertCollection(Object value) {
        if (value instanceof Collection) {
            return true;
        }
        throw new SpecificationException("After operator IN should be a list, not '" + value + "'");
    }

    private Path<Object> parsePath(Path<? extends Object> root, String field) {
        if (!field.contains(PATH_DELIMITER)) {
            return root.get(field);
        }
        int i = field.indexOf(PATH_DELIMITER);
        String firstPart = field.substring(0, i);
        String secondPart = field.substring(i + 1);
        Path<Object> p = root.get(firstPart);
        return parsePath(p, secondPart);
    }

    public static void setDefaultDateFormat(SimpleDateFormat defaultDateFormat) {
        WhereSpecification.defaultDateFormat = defaultDateFormat;
    }

    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }
}
