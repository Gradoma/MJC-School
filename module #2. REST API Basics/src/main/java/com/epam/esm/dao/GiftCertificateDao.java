package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateDao {
    long add(GiftCertificate certificate);
    List<GiftCertificate> findAllWithTags();
    List<GiftCertificate> findAll();
    List<GiftCertificate> findByTag(long tagId);
    GiftCertificate findById(long id);
    GiftCertificate findByName(String name);
    List<GiftCertificate> findByDescription(String description);
    boolean update(GiftCertificate certificate);
    boolean delete(long id);
}
