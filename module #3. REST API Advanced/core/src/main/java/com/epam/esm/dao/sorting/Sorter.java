package com.epam.esm.dao.sorting;

public class Sorter {
    private static final String ORDER_BY = " ORDER BY ";

    public static String prepareSorting(String columnName, Order orderEnum){
        return ORDER_BY + columnName + orderEnum.getValue();
    }
}
