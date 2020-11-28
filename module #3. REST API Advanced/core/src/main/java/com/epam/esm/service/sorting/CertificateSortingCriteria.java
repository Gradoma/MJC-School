package com.epam.esm.service.sorting;

import com.epam.esm.dao.column.GiftCertificateTableConst;

public enum CertificateSortingCriteria {
    DATE(GiftCertificateTableConst.LAST_UPDATE_DATE),
    NAME(GiftCertificateTableConst.NAME);

    private final String column;

    CertificateSortingCriteria(String column){
        this.column = column;
    }

    public String getColumn() {
        return column;
    }
}
