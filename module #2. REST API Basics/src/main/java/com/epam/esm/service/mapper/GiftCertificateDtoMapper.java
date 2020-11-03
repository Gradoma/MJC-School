package com.epam.esm.service.mapper;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateDto;
import com.epam.esm.entity.Tag;
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
        giftCertificate.setPrice(Double.parseDouble(certificateDto.getPrice()));
        long days = Long.parseLong(certificateDto.getDuration());
        giftCertificate.setDuration(Duration.ofDays(days));
        giftCertificate.setCreateDate(ZonedDateTime.parse(certificateDto.getCreateDate()));
        giftCertificate.setLastUpdateDate(ZonedDateTime.parse(certificateDto.getLastUpdateDate()));
        return giftCertificate;
    }

    public GiftCertificateDto toDto(GiftCertificate giftCertificate){
        GiftCertificateDto certificateDto = new GiftCertificateDto();
        certificateDto.setId(Long.toString(giftCertificate.getId()));
        certificateDto.setName(giftCertificate.getName());
        certificateDto.setDescription(giftCertificate.getDescription());
        certificateDto.setPrice(Double.toString(giftCertificate.getPrice()));
        certificateDto.setCreateDate(giftCertificate.getCreateDate().toString());
        certificateDto.setLastUpdateDate(giftCertificate.getLastUpdateDate().toString());
        certificateDto.setDuration(giftCertificate.getDuration().toString());
        certificateDto.setTags(convertToTagNamesArray(giftCertificate.getTagList()));
        return certificateDto;
    }

    public List<GiftCertificateDto> toDto(List<GiftCertificate> certificateList){
        List<GiftCertificateDto> resultList = new ArrayList<>();
        for(GiftCertificate certificate : certificateList){
            resultList.add(toDto(certificate));
        }
        return resultList;
    }

    private String[] convertToTagNamesArray(List<Tag> tagList){
        List<String> nameList = new ArrayList<>();
        for(Tag tag : tagList){
            nameList.add(tag.getName());
        }
        return nameList.stream().toArray(String[]::new);
    }
}
