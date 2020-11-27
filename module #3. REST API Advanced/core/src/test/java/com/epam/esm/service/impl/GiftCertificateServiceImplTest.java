package com.epam.esm.service.impl;

import com.epam.esm.config.SpringTestConfig;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {SpringTestConfig.class})
class GiftCertificateServiceImplTest {
    @Autowired
    private GiftCertificateService tagService;


    @Test
    void addNegative_ValidationDate() {
        GiftCertificateDto certificateDto = new GiftCertificateDto();
        certificateDto.setName("name");
        certificateDto.setPrice(23.55);
        certificateDto.setDescription("descript");
        certificateDto.setCreateDate("incorrect date");
        certificateDto.setLastUpdateDate("2020-08-29T06:12:15.156-03:00");
        certificateDto.setDuration(23L);

        assertThrows(ConstraintViolationException.class, () -> tagService.add(certificateDto));
    }
}