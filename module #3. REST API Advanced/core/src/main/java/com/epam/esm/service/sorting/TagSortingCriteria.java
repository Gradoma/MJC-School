package com.epam.esm.service.sorting;

public enum TagSortingCriteria {
    ID("t.id"),
    NAME("t.name");

    private final String fieldName;

    TagSortingCriteria(String fieldName){
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
