package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;

import javax.validation.Valid;
import java.util.List;

public interface GiftCertificateService {
    long add(@Valid GiftCertificateDto certificateDto);
    GiftCertificateDto getById(long id);
    List<GiftCertificateDto> getByCriteria(String tag, String name, String description, String sortBy, String order);
    boolean update(@Valid GiftCertificateDto certificateDto, long certificateId);
    boolean delete(long id);
}
