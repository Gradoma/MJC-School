package com.epam.esm.dao.criteria;

import com.epam.esm.dao.criteria.impl.*;

import java.util.Arrays;

public enum CriteriaSet {
    BY_TAG("Tag", new TagSelector()),
    BY_TAG_AND_NAME("TagName", new TagNameSelector()),
    BY_NAME("Name", new NameSelector()),
    BY_TAG_AND_DESCRIPTION("TagDescription", new TagDescriptionSelector()),
    BY_DESCRIPTION("Description", new DescriptionSelector()),
    BY_TAG_AND_NAME_AND_DESCRIPTION("TagNameDescription", new TagNameDescriptionSelector()),
    BY_NAME_AND_DESCRIPTION("NameDescription", new NameDescriptionSelector()),
    NO_CRITERIA("noCriteria", new DefaultSelector());

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
        return Arrays.stream(CriteriaSet.values())
                .filter(criteriaSet -> criteriaSet.getCriteriaName().equals(name))
                .findFirst()
                .orElse(NO_CRITERIA);
    }
}
