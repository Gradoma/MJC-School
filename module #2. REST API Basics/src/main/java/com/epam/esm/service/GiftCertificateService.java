package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificateDto;
import com.epam.esm.exception.InvalidParameterException;

import java.util.List;

public interface GiftCertificateService {
    long add(GiftCertificateDto certificateDto) throws InvalidParameterException;
    List<GiftCertificateDto> getAll();
    GiftCertificateDto getById(long id);
    List<GiftCertificateDto> getByName(String name);
    List<GiftCertificateDto> getByDescription(String name);
    List<GiftCertificateDto> getByTag(String tagName) throws InvalidParameterException;
    boolean update(GiftCertificateDto certificateDto, long certificateId) throws InvalidParameterException;
    boolean delete(long id);
}
