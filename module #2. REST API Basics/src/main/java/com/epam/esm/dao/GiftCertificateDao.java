package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateDao {
    long add(GiftCertificate certificate);
    List<GiftCertificate> findAll();
    GiftCertificate findById(long id);
    GiftCertificate findByName(String name);
    GiftCertificate findByDescription(String description);
    boolean update(GiftCertificate certificate);
    boolean delete(long id);
}
