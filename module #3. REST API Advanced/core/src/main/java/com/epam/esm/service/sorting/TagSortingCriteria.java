package com.epam.esm.service.sorting;

import com.epam.esm.dao.column.GiftCertificateTableConst;
import com.epam.esm.dao.column.TagTableConst;

public enum TagSortingCriteria {
    ID(TagTableConst.ID),
    NAME(TagTableConst.NAME);

    private final String column;

    TagSortingCriteria(String column){
        this.column = column;
    }

    public String getColumn() {
        return column;
    }
}
