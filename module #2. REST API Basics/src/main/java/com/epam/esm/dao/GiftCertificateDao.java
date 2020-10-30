package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateDao {
    long add(GiftCertificate certificate);
    List<GiftCertificate> findAll() throws DaoException;
    List<GiftCertificate> findByTag(long tagId) throws DaoException;
    Optional<GiftCertificate> findById(long id) throws DaoException;
    Optional<GiftCertificate> findByName(String name) throws DaoException;
    Optional<GiftCertificate> findByDescription(String description) throws DaoException;
    boolean update(GiftCertificate certificate);
    boolean delete(long id);
}
