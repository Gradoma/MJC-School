package com.epam.esm.dao.criteria;

import com.epam.esm.dao.criteria.impl.*;

public enum CriteriaSet {
    BY_TAG("Tag", new SelectorByTag()),
    BY_CERTIFICATE_PARAM("CertificateParam", new SelectorByCertificateParam());

    private String criteriaName;
    private GiftCertificateSelector selector;

    CriteriaSet(String criteriaName, GiftCertificateSelector selector){
        this.criteriaName = criteriaName;
        this.selector = selector;
    }

    public GiftCertificateSelector getSelector(){
        return selector;
    }
    private String getCriteriaName(){
        return criteriaName;
    }

    public static CriteriaSet getByName(String name){
        if(name.contains("Tag")){
            return BY_TAG;
        } else {
            return BY_CERTIFICATE_PARAM;
        }
    }
}
