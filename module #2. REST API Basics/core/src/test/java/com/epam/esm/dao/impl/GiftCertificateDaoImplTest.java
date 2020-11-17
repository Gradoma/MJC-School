package com.epam.esm.dao.impl;

import com.epam.esm.config.SpringTestConfig;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.column.GiftCertificateTableConst;
import com.epam.esm.dao.column.TagTableConst;
import com.epam.esm.dao.column.TagToCertificateTableConst;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.validation.constraints.Digits;
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
@SqlGroup({
        @Sql("/db/certificate-data.sql"),
        @Sql("/db/tag-data.sql")
})
class GiftCertificateDaoImplTest {      //todo (independent tests)
    private static final String DESCRIPTION_KEYWORD_DESCRIPTION = "description";
    private static final int DESCRIPTION_COUNT = 3;
    private static final String NAME_KEYWORD_TEXT = "name";
    private static final int TEXT_COUNT = 3;
    private static GiftCertificate certificate;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private GiftCertificateDao giftCertificateDao;

    @BeforeAll
    static void beforeAll() {
        Tag tagSport = new Tag();
        tagSport.setId(1);
        tagSport.setName("sport");
        Tag tagShops = new Tag();
        tagShops.setId(4);
        tagShops.setName("shops");
        certificate = new GiftCertificate();
        certificate.setName("Name 1");
        certificate.setDescription("some description");
        certificate.setPrice(52.23);
        ZonedDateTime creationDate = ZonedDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        certificate.setCreateDate(creationDate);
        certificate.setLastUpdateDate(certificate.getCreateDate());
        certificate.setDuration(Duration.ofDays(17));
        certificate.addTag(tagShops);
        certificate.addTag(tagSport);
    }

    @AfterEach
    void cleanUp(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, GiftCertificateTableConst.TABLE_CERTIFICATE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, TagToCertificateTableConst.TABLE_TAG_CERT);
    }

    @Test
    void add() {
        int startRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, GiftCertificateTableConst.TABLE_CERTIFICATE);
        int startRowsInTagToCertTable = JdbcTestUtils.countRowsInTable(jdbcTemplate,
                TagToCertificateTableConst.TABLE_TAG_CERT);
        long generatedId = giftCertificateDao.add(certificate);
        certificate.setId(generatedId);
        assertEquals(startRows + 1,
                JdbcTestUtils.countRowsInTable(jdbcTemplate, GiftCertificateTableConst.TABLE_CERTIFICATE));
        assertEquals(startRowsInTagToCertTable + certificate.getTagSet().size(),
                JdbcTestUtils.countRowsInTable(jdbcTemplate, TagToCertificateTableConst.TABLE_TAG_CERT));
    }

    @Test
    void findById() {
        long id = giftCertificateDao.add(certificate);
        certificate.setId(id);
        GiftCertificate fromDb = giftCertificateDao.findById(id);
        ZonedDateTime createDate = fromDb.getCreateDate();
        fromDb.setCreateDate(createDate.withZoneSameInstant(ZoneId.systemDefault()));
        ZonedDateTime lastUpdDate = fromDb.getLastUpdateDate();
        fromDb.setLastUpdateDate(lastUpdDate.withZoneSameInstant(ZoneId.systemDefault()));
        assertEquals(certificate, fromDb);
    }
}