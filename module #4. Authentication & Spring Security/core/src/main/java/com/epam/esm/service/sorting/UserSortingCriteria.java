package com.epam.esm.service.sorting;

public enum UserSortingCriteria {
    ID("u.id"),
    NAME("u.name");

    private final String fieldName;

    UserSortingCriteria(String fieldName){
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
