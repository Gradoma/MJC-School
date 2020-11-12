package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateDao {
    String BY_TAG = "Tag";
    String BY_NAME = "Name";
    String BY_DESCRIPTION = "Description";
    String BY_NAME_AND_DESCRIPTION = "NameDescription";
    String BY_TAG_AND_NAME = "TagName";
    String BY_TAG_AND_DESCRIPTION= "TagDescription";
    String BY_TAG_AND_NAME_AND_DESCRIPTION = "TagNameDescription";
    String NO_CRITERIA = "noCriteria";

    long add(GiftCertificate certificate);
    List<GiftCertificate> findByCriteria(String criteriaSet, String tagName, String name,
                                         String description);
    GiftCertificate findById(long id);
    boolean update(GiftCertificate certificate, List<Long> addedTagsId, List<Long> deletedTagsId);
    boolean delete(long id);
}
