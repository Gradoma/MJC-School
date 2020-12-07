package com.epam.esm.service.mapper;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class GiftCertificateDtoMapper {
    public GiftCertificate toEntity(GiftCertificateDto certificateDto){
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName(certificateDto.getName());
        giftCertificate.setDescription(certificateDto.getDescription());
        giftCertificate.setPrice(certificateDto.getPrice());
        long days = certificateDto.getDuration();
        giftCertificate.setDuration(Duration.ofDays(days));
        return giftCertificate;
    }

    public GiftCertificateDto toDto(GiftCertificate giftCertificate){
        GiftCertificateDto certificateDto = new GiftCertificateDto();
        certificateDto.setId(giftCertificate.getId());
        certificateDto.setName(giftCertificate.getName());
        certificateDto.setDescription(giftCertificate.getDescription());
        certificateDto.setPrice(giftCertificate.getPrice());
        certificateDto.setCreateDate(giftCertificate.getCreateDate().toString());
        certificateDto.setLastUpdateDate(giftCertificate.getLastUpdateDate().toString());
        certificateDto.setDuration(giftCertificate.getDuration().toDays());
        return certificateDto;
    }

    public List<GiftCertificateDto> toDto(List<GiftCertificate> certificateList){
        List<GiftCertificateDto> resultList = new ArrayList<>();
        certificateList.forEach(certificate -> resultList.add(toDto(certificate)));
        return resultList;
    }
}
