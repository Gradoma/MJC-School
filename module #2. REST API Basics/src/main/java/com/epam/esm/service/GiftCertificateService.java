package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.exception.InvalidEntityException;

import javax.validation.Valid;
import java.util.List;

public interface GiftCertificateService {
    long add(@Valid GiftCertificateDto certificateDto);
    List<GiftCertificateDto> getAll();
    GiftCertificateDto getById(long id);
    List<GiftCertificateDto> getByName(String name);
    List<GiftCertificateDto> getByDescription(String name);
    List<GiftCertificateDto> getByTag(String tagName) throws InvalidEntityException;
    boolean update(GiftCertificateDto certificateDto, long certificateId) throws InvalidEntityException;
    boolean delete(long id);
}
