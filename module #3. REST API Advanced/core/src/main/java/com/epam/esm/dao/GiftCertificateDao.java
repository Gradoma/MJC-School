package com.epam.esm.dao;

import com.epam.esm.dao.criteria.QueryCriteria;
import com.epam.esm.dto.CertificateCriteria;
import com.epam.esm.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateDao {
    long add(GiftCertificate certificate);
    List<GiftCertificate> findByCriteria(QueryCriteria queryCriteria);
    GiftCertificate findById(long id);
    boolean update(GiftCertificate certificate);
    boolean patch(GiftCertificate certificate);
    boolean delete(long id);
}
