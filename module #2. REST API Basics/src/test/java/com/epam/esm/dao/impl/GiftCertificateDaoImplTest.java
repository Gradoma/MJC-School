package com.epam.esm.dao.impl;

import com.epam.esm.config.SpringTestConfig;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.column.GiftCertificateTableConst;
import com.epam.esm.dao.column.TagTableConst;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringTestConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GiftCertificateDaoImplTest {      //todo (independent tests)
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private GiftCertificateDao giftCertificateDao;
    private static GiftCertificate certificate;

    @BeforeAll
    static void beforeAll() {
        certificate = new GiftCertificate();
        certificate.setName("Name 1");
        certificate.setDescription("some description");
        certificate.setPrice(52.23);
        ZonedDateTime creationDate = ZonedDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        certificate.setCreateDate(creationDate);
        certificate.setLastUpdateDate(certificate.getCreateDate());
        certificate.setDuration(Duration.ofDays(17));
    }

    @Test
    @Order(1)
    void add() {
        int startRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, GiftCertificateTableConst.TABLE_CERTIFICATE);
        long generatedId = giftCertificateDao.add(certificate);
        certificate.setId(generatedId);
        assertEquals(startRows + 1,
                JdbcTestUtils.countRowsInTable(jdbcTemplate, GiftCertificateTableConst.TABLE_CERTIFICATE));
    }

    @Test
    @Order(2)
    void findById() {
        GiftCertificate fromDb = giftCertificateDao.findById(certificate.getId());
        ZonedDateTime createDate = fromDb.getCreateDate();
        fromDb.setCreateDate(createDate.withZoneSameInstant(ZoneId.systemDefault()));
        ZonedDateTime lastUpdDate = fromDb.getLastUpdateDate();
        fromDb.setLastUpdateDate(lastUpdDate.withZoneSameInstant(ZoneId.systemDefault()));
        assertEquals(certificate, fromDb);
    }

    @Test
    @Order(3)
    void findByName() {
        GiftCertificate fromDb = giftCertificateDao.findByName("incorrect name");
        ZonedDateTime createDate = fromDb.getCreateDate();
        fromDb.setCreateDate(createDate.withZoneSameInstant(ZoneId.systemDefault()));
        ZonedDateTime lastUpdDate = fromDb.getLastUpdateDate();
        fromDb.setLastUpdateDate(lastUpdDate.withZoneSameInstant(ZoneId.systemDefault()));
        assertEquals(certificate, fromDb);
    }
}