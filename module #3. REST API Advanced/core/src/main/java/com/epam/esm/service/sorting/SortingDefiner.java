package com.epam.esm.service.sorting;

import com.epam.esm.dao.column.GiftCertificateTableConst;
import com.epam.esm.dao.sorting.Order;
import com.epam.esm.exception.InvalidSortingException;

public class SortingDefiner {
    public static final String NAME = "name";
    public static final String DATE = "date";

    public static Order defineOrder(String order){
        if("asc".equalsIgnoreCase(order)){
            return Order.ASC;
        }
        if("desc".equalsIgnoreCase(order)){
            return Order.DESC;
        }
        throw new InvalidSortingException("order: " + order);
    }

    public static String defineColumn(String sortByName){
        switch (sortByName){
            case NAME:
                return GiftCertificateTableConst.NAME;
            case DATE:
                return GiftCertificateTableConst.LAST_UPDATE_DATE;
            default:
                throw new InvalidSortingException(" name=" + sortByName);
        }
    }
}
