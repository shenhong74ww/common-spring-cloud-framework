package com.middleware.common.service.impl;

import com.middleware.common.exception.UnsupportedSortFieldException;
import com.middleware.common.exception.WrongIdFormatException;
import com.middleware.common.model.BaseModel;
import com.google.common.collect.Lists;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class BaseServiceImpl {
    final int SORT_PARAM_INDEX = 0;
    final int SORT_TYPE_INDEX = 1;
    final int ONLY_HAVE_PARAM_NAME = 1;
    final String DEFAULT_SORT_TYPE = "asc";

    public Sort multipleConditionsSort(String sortBy, Class<?> cla) {
        List<Sort.Order> orderList = new ArrayList<>();
        Sort sort = null;

        if (!StringUtils.isEmpty(sortBy)) {
            String[] spliceFromSortBy = sortBy.split(",");
            List<String> initialList = Lists.newArrayList();
            List<String> fieldNames = getAllFieldNames(cla, initialList, null);
            Field[] fields = cla.getSuperclass().getDeclaredFields();
            for (Field field : fields) {
                fieldNames.add(field.getName());
            }
            for (String sortOpreation : spliceFromSortBy) {
                String[] spliceFromSortOpreation = sortOpreation.trim().split(" ");
                String sortParam = spliceFromSortOpreation[SORT_PARAM_INDEX];

                Stream<String> fieldStream = fieldNames.stream();
                if (fieldStream.distinct().filter(fieldName -> fieldName.equals(sortParam)).findFirst().orElse(null) == null) {
                    throw new UnsupportedSortFieldException(String.format("The parameter '%s' is not supported to sort", sortParam));
                }

                String sortType = null;

                if (spliceFromSortOpreation.length == ONLY_HAVE_PARAM_NAME) {
                    sortType = DEFAULT_SORT_TYPE;
                } else {
                    sortType = spliceFromSortOpreation[SORT_TYPE_INDEX];
                }

                sortType = sortType.toUpperCase();

                // check order type
                if (!Sort.Direction.ASC.toString().equals(sortType)
                        && !Sort.Direction.DESC.toString().equals(sortType)) {
                    throw new WrongIdFormatException("Sort type value can be: asc or desc");
                }

                Sort.Direction directionSortType = Sort.Direction.ASC.toString().equals(sortType) ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

                orderList.add(new Sort.Order(directionSortType, sortParam));
            }

            sort = new Sort(orderList);
        }

        return sort;
    }

    private List<String> getAllFieldNames(Class<?> cla, List<String> fieldNames, String namePrefix) {
        Field[] fields = cla.getDeclaredFields();

        for (Field field : fields) {
            if (field.getType().getSuperclass() == BaseModel.class) {
                String newNamePrefix;
                if (namePrefix == null) {
                    newNamePrefix = field.getName() + ".";
                } else {
                    newNamePrefix = namePrefix + field.getName() + ".";
                }
                getAllFieldNames(field.getType(), fieldNames, newNamePrefix);
            }

            if (namePrefix == null) {
                fieldNames.add(field.getName());
            } else {
                fieldNames.add(namePrefix + field.getName());
            }

        }

        return fieldNames;
    }
}
