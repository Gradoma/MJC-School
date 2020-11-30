package com.epam.esm.service.sorting;

public enum TagSortingCriteria {
    ID("t.id"),
    NAME("t.name");

    private final String column;

    TagSortingCriteria(String column){
        this.column = column;
    }

    public String getColumn() {
        return column;
    }
}
