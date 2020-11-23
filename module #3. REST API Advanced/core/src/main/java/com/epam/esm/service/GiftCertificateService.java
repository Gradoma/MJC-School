package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;

import java.util.List;

public interface GiftCertificateService {
    long add(GiftCertificateDto certificateDto);
    GiftCertificateDto getById(long id);
    List<GiftCertificateDto> getByCriteria(List<String> tags, String name, String description, String sortBy, String order);
    boolean update(GiftCertificateDto certificateDto, long certificateId);
    boolean delete(long id);
}
