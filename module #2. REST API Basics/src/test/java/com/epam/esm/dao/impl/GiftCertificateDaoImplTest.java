package com.epam.esm.dao.impl;

import com.epam.esm.config.SpringTestConfig;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.column.GiftCertificateTableConst;
import com.epam.esm.dao.column.TagTableConst;
import com.epam.esm.dao.column.TagToCertificateTableConst;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateException;
import com.epam.esm.exception.ResourceNotFoundException;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringTestConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SqlGroup({
        @Sql("/db/certificate-data.sql")
})
class GiftCertificateDaoImplTest {
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
    void containsDbCredentials(){
        String user = null;
        String pass = null;
        Map<String, String> map = System.getenv();
        for (Map.Entry <String, String> entry: map.entrySet()) {
            if(entry.getKey().equalsIgnoreCase("DB_USER")){
                user = entry.getKey();
                System.out.println(entry.getValue());
            }
            if(entry.getKey().equalsIgnoreCase("DB_PASS")){
                pass = entry.getKey();
            }
        }
        assertEquals("DB_USER", user);
        assertEquals("DB_PASS", pass);
    }

    @Test
    void findById() {
        GiftCertificate cert = new GiftCertificate();
        cert.setName("Name 2");
        cert.setDescription("Desc 2");
        cert.setPrice(35.15);
        ZonedDateTime creationDate = ZonedDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        cert.setCreateDate(creationDate);
        cert.setLastUpdateDate(cert.getCreateDate());
        cert.setDuration(Duration.ofDays(20));

        long id = giftCertificateDao.add(cert);
        cert.setId(id);
        GiftCertificate fromDb = giftCertificateDao.findById(id);
        ZonedDateTime createDate = fromDb.getCreateDate();
        fromDb.setCreateDate(createDate.withZoneSameInstant(ZoneId.systemDefault()));
        ZonedDateTime lastUpdDate = fromDb.getLastUpdateDate();
        fromDb.setLastUpdateDate(lastUpdDate.withZoneSameInstant(ZoneId.systemDefault()));
        assertEquals(cert, fromDb);
    }

    @Test
    void findById_negative(){
        GiftCertificate cert = new GiftCertificate();
        cert.setName("Name 2");
        cert.setDescription("Desc 2");
        cert.setPrice(35.15);
        ZonedDateTime creationDate = ZonedDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        cert.setCreateDate(creationDate);
        cert.setLastUpdateDate(cert.getCreateDate());
        cert.setDuration(Duration.ofDays(20));
        long id = giftCertificateDao.add(cert);

        long incorrectId = id * 31;
        assertThrows(ResourceNotFoundException.class, () -> giftCertificateDao.findById(incorrectId));
    }

    @Test
    void findByCriteria(){

        GiftCertificate certABC = new GiftCertificate();
        certABC.setName("Cert B");
        certABC.setDescription("Desc B");
        certABC.setPrice(52.23);
        ZonedDateTime creationDate = ZonedDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        certABC.setCreateDate(creationDate);
        certABC.setLastUpdateDate(certABC.getCreateDate());
        certABC.setDuration(Duration.ofDays(17));

        GiftCertificate certBC = new GiftCertificate();
        certBC.setName("Cert B C");
        certBC.setDescription("Desc B C");
        certBC.setPrice(52.23);
        ZonedDateTime creationDateBC = ZonedDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        certBC.setCreateDate(creationDateBC);
        certBC.setLastUpdateDate(certBC.getCreateDate());
        certBC.setDuration(Duration.ofDays(17));

        GiftCertificate certAC = new GiftCertificate();
        certAC.setName("Cert A C");
        certAC.setDescription("Desc A C");
        certAC.setPrice(52.23);
        ZonedDateTime creationDateAC = ZonedDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        certAC.setCreateDate(creationDateAC);
        certAC.setLastUpdateDate(certAC.getCreateDate());
        certAC.setDuration(Duration.ofDays(17));

        giftCertificateDao.add(certABC);
        giftCertificateDao.add(certBC);
        giftCertificateDao.add(certAC);

        List<GiftCertificate> certList2 = giftCertificateDao.findByCriteria("NameDescription",
                null, "a", "desc");
        assertEquals(3, certList2.size());
    }

    @Test
    void delete(){
        GiftCertificate cert = new GiftCertificate();
        cert.setName("Name 2");
        cert.setDescription("Desc 2");
        cert.setPrice(35.15);
        ZonedDateTime creationDate = ZonedDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        cert.setCreateDate(creationDate);
        cert.setLastUpdateDate(cert.getCreateDate());
        cert.setDuration(Duration.ofDays(20));

        long id = giftCertificateDao.add(cert);
        int rowsAfterAdd = JdbcTestUtils.countRowsInTable(jdbcTemplate,
                GiftCertificateTableConst.TABLE_CERTIFICATE);
        giftCertificateDao.delete(id);
        assertEquals(rowsAfterAdd - 1, JdbcTestUtils.countRowsInTable(jdbcTemplate,
                GiftCertificateTableConst.TABLE_CERTIFICATE));
    }


}