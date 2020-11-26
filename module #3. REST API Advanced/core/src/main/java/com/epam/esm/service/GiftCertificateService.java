package com.epam.esm.service;

import com.epam.esm.dto.CertificateCriteria;
import com.epam.esm.dto.GiftCertificateDto;

import java.util.List;

public interface GiftCertificateService {
    long add(GiftCertificateDto certificateDto);
    GiftCertificateDto getById(long id);
    List<GiftCertificateDto> getByCriteria(CertificateCriteria criteria);
    boolean update(GiftCertificateDto certificateDto, long certificateId);
    boolean delete(long id);
}
