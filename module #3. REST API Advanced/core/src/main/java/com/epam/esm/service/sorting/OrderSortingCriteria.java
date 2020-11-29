package com.epam.esm.service.sorting;

import com.epam.esm.dao.column.OrdersTableConst;
import com.epam.esm.dao.column.TagTableConst;

public enum OrderSortingCriteria {
    ID(OrdersTableConst.ID),
    DATE(OrdersTableConst.PURCHASE_TIME);

    private final String column;

    OrderSortingCriteria(String column){
        this.column = column;
    }

    public String getColumn() {
        return column;
    }
}
