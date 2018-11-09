package com.middleware.common.specification;

import com.middleware.common.config.DynamicQueryFilterLogicType;
import com.middleware.common.config.DynamicQueryOperators;

import java.util.List;

public class Filter {
    public static final String PATH_DELIMITER = ".";

    private String field;
    private DynamicQueryOperators operator;
    private Object value;
    private DynamicQueryFilterLogicType logic;
    private List<Filter> filters;

    public Filter() {
    }

    public Filter(String field, DynamicQueryOperators operator, Object value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public DynamicQueryOperators getOperator() {
        return operator;
    }

    public void setOperator(DynamicQueryOperators operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public DynamicQueryFilterLogicType getLogic() {
        return logic;
    }

    public void setLogic(DynamicQueryFilterLogicType logic) {
        this.logic = logic;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }
}
