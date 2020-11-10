package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GiftCertificateDao {
    long add(GiftCertificate certificate);
    List<GiftCertificate> findAllWithTags();
    List<GiftCertificate> findAll();
    List<GiftCertificate> findByTag(long tagId);
    List<GiftCertificate> findByCriteria(Map<String, String> criteriaMap);
    GiftCertificate findById(long id);
    List<GiftCertificate> findByName(String name);
    List<GiftCertificate> findByDescription(String description);
    boolean update(GiftCertificate certificate, List<Long> addedTagsId, List<Long> deletedTagsId);
    boolean delete(long id);
}
