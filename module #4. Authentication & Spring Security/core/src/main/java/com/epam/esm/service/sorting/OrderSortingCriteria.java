package com.epam.esm.service.sorting;

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
