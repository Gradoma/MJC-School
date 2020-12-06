package com.epam.esm.service.impl;

import com.epam.esm.config.TestApp;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.CertificateCriteria;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApp.class)
class GiftCertificateServiceImplTest {
    @Autowired
    private GiftCertificateService giftCertificateService;
//    @Autowired
//    private GiftCertificateDtoMapper giftMapper;
//    @Autowired
//    private TagDtoMapper tagDtoMapper;

    @MockBean
    private TagDao tagDao;

    @MockBean
    private GiftCertificateDao certificateDao;

    @MockBean
    private TagService tagService;

    @Test
    void add() {
        GiftCertificateDto certificateDto = new GiftCertificateDto();
        certificateDto.setName("name");
        certificateDto.setPrice(23.55);
        certificateDto.setDescription("Description");
        certificateDto.setDuration(23L);

        List<TagDto> tagDtoList = new ArrayList<>();
        TagDto tag1 = new TagDto();
        tag1.setName("exist tag 1");
        tagDtoList.add(tag1);
        TagDto tag2 = new TagDto();
        tag2.setName("exist tag 2");
        tagDtoList.add(tag2);
        certificateDto.setTags(tagDtoList);

        ArgumentCaptor<GiftCertificate> captor = ArgumentCaptor.forClass(GiftCertificate.class);

        giftCertificateService.add(certificateDto);

        // dates, tags were set
        Mockito.verify(certificateDao, Mockito.times(1))
                .add(captor.capture());
        GiftCertificate certificate = captor.getValue();
        assertNotNull(certificate.getCreateDate());
        assertNotNull(certificate.getLastUpdateDate());
        assertNotNull(certificate.getTagSet());

        //tags checking was called
        Mockito.verify(tagService, Mockito.times(2))
                .doesExist(ArgumentMatchers.any());
        //cert dao was called
        Mockito.verify(certificateDao, Mockito.times(1))
                .add(ArgumentMatchers.eq(certificate));

    }

    @Test
    void addWithNewTag() {
        GiftCertificateDto certificateDto = new GiftCertificateDto();
        certificateDto.setName("name");
        certificateDto.setPrice(23.55);
        certificateDto.setDescription("Description");
        certificateDto.setDuration(23L);

        List<TagDto> tagDtoList = new ArrayList<>();
        TagDto tag1 = new TagDto();
        tag1.setName("exist tag 1");
        tagDtoList.add(tag1);
        TagDto tag2 = new TagDto();
        tag2.setName("new tag");
        tagDtoList.add(tag2);
        certificateDto.setTags(tagDtoList);

        Mockito.doReturn(false).when(tagService).doesExist(tag2);

        ArgumentCaptor<GiftCertificate> certificateCaptor = ArgumentCaptor.forClass(GiftCertificate.class);

        giftCertificateService.add(certificateDto);

        // dates, tags were set
        Mockito.verify(certificateDao, Mockito.times(1))
                .add(certificateCaptor.capture());
        GiftCertificate certificate = certificateCaptor.getValue();
        assertNotNull(certificate.getCreateDate());
        assertNotNull(certificate.getLastUpdateDate());
        assertNotNull(certificate.getTagSet());

        //tags checking was called
        Mockito.verify(tagService, Mockito.times(2))
                .doesExist(ArgumentMatchers.any());
        //one tag was added
        Mockito.verify(tagService, Mockito.times(1))
                .save(ArgumentMatchers.eq(tag2));
        //cert dao was called
        Mockito.verify(certificateDao, Mockito.times(1))
                .add(ArgumentMatchers.eq(certificate));

    }

    @Test
    void getByCriteria(){
        CertificateCriteria criteria = new CertificateCriteria();
        criteria.setPage(2);

        List<GiftCertificate> certificateList = new ArrayList<>();
        GiftCertificate certificate1 = new GiftCertificate();
        certificate1.setId(1);
        certificate1.setCreateDate(ZonedDateTime.now());
        certificate1.setLastUpdateDate(ZonedDateTime.now());
        certificate1.setDuration(Duration.ofDays(2));
        certificateList.add(certificate1);
        GiftCertificate certificate2 = new GiftCertificate();
        certificate2.setId(2);
        certificate2.setCreateDate(ZonedDateTime.now());
        certificate2.setLastUpdateDate(ZonedDateTime.now());
        certificate2.setDuration(Duration.ofDays(2));
        certificateList.add(certificate2);

        Mockito.doReturn(certificateList).when(certificateDao).findByCriteria(ArgumentMatchers.eq(criteria));

        giftCertificateService.getByCriteria(criteria);
        // order and sorting were set
        assertNotNull(criteria.getSortingCriteria());
        assertNotNull(criteria.getSortingOrder());
        assertEquals(5, criteria.getFirstResult());
        assertEquals(5, criteria.getResultLimit());
        //cert dao was called 1 time
        Mockito.verify(certificateDao, Mockito.times(1))
                .findByCriteria(ArgumentMatchers.eq(criteria));
    }

    @Test
    void patch(){
        GiftCertificateDto certificateDto = new GiftCertificateDto();
        certificateDto.setName("name");
        long id = 2;

        ArgumentCaptor<GiftCertificate> certificateCaptor = ArgumentCaptor.forClass(GiftCertificate.class);

        giftCertificateService.patch(certificateDto, id);

        // get and check fields of cert were set
        Mockito.verify(certificateDao, Mockito.times(1))
                .patch(certificateCaptor.capture());
        GiftCertificate certificate = certificateCaptor.getValue();
        assertEquals("name", certificate.getName());
        assertEquals(2, certificate.getId());
        assertNotNull(certificate.getLastUpdateDate());
    }

    @Test
    void getById(){
        long id = 2;
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(id);
        certificate.setCreateDate(ZonedDateTime.now());
        certificate.setLastUpdateDate(ZonedDateTime.now());
        certificate.setDuration(Duration.ofDays(12));

        Mockito.doReturn(certificate).when(certificateDao).findById(id);

        giftCertificateService.getById(id);

        Mockito.verify(tagService, Mockito.times(1))
                .getByGiftCertificateId(ArgumentMatchers.eq(id));
    }

    @Test
    void update(){
        long certificateId = 3;
        GiftCertificate originalCert = new GiftCertificate();
        originalCert.setId(certificateId);
        originalCert.setCreateDate(ZonedDateTime.now());
        originalCert.setLastUpdateDate(ZonedDateTime.now().minusDays(2));
        originalCert.setDuration(Duration.ofDays(3));

        Mockito.doReturn(originalCert).when(certificateDao).findById(ArgumentMatchers.eq(certificateId));

        GiftCertificateDto updatedCertificate = new GiftCertificateDto();
        updatedCertificate.setName("name");
        updatedCertificate.setPrice(23.55);
        updatedCertificate.setDescription("Description");
        updatedCertificate.setDuration(23L);

        List<TagDto> tagDtoList = new ArrayList<>();
        TagDto tag1 = new TagDto();
        tag1.setName("exist tag 1");
        tagDtoList.add(tag1);
        TagDto tag2 = new TagDto();
        tag2.setName("exist tag 2");
        tagDtoList.add(tag2);
        updatedCertificate.setTags(tagDtoList);

        ArgumentCaptor<GiftCertificate> captor = ArgumentCaptor.forClass(GiftCertificate.class);

        giftCertificateService.update(updatedCertificate, certificateId);

        // dates, tags were set
        Mockito.verify(certificateDao, Mockito.times(1))
                .update(captor.capture());
        GiftCertificate certificate = captor.getValue();
        assertEquals(certificateId, certificate.getId());
        assertNotNull(certificate.getCreateDate());
        assertNotNull(certificate.getLastUpdateDate());
        assertNotNull(certificate.getTagSet());

        //tags checking was called
        Mockito.verify(tagService, Mockito.times(2))
                .doesExist(ArgumentMatchers.any());
    }
}