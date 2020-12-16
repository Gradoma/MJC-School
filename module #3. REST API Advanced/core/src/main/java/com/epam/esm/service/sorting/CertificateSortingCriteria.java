package com.epam.esm.service.sorting;

import com.epam.esm.dao.column.GiftCertificateTableConst;

public enum CertificateSortingCriteria {
    DATE(GiftCertificateTableConst.LAST_UPDATE_DATE),
    NAME(GiftCertificateTableConst.NAME);

    private final String fieldName;

    CertificateSortingCriteria(String fieldName){
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
