package com.epam.esm.dao;

import com.epam.esm.dao.sorting.Order;
import com.epam.esm.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateDao {
    long add(GiftCertificate certificate);
    List<GiftCertificate> findByCriteria(String criteriaSet, List<String> tagNames, String name,
                                         String description, String sortByColumn, Order orderEnum);
    GiftCertificate findById(long id);
    boolean update(GiftCertificate certificate, List<Long> addedTagsId, List<Long> deletedTagsId);
    boolean delete(long id);
}
