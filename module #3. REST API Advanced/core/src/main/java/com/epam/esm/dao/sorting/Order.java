package com.epam.esm.dao.sorting;

public enum Order {
    ASC(" ASC"), DESC(" DESC");

    private String value;

    Order(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
