package com.epam.esm.service.sorting;

import com.epam.esm.dao.column.OrdersTableConst;

public enum OrderSortingCriteria {
    ID("o.id"),
    DATE("o.purchaseDate");

    private final String fieldName;

    OrderSortingCriteria(String fieldName){
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
