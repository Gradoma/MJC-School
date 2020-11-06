package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.column.TagTableConst;
import com.epam.esm.config.SpringTestConfig;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Sql("/db/tag-data.sql")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringTestConfig.class})
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TagDaoImplTest {
    private static final String EXIST_NAME = "sport";
    private static int startRows;
    private static long firstId = 1;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TagDao tagDao;

    @BeforeEach
    void before() {
        startRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, TagTableConst.TABLE_TAG);
    }

    @AfterEach
    void after() {
        firstId = 1 + JdbcTestUtils.countRowsInTable(jdbcTemplate, TagTableConst.TABLE_TAG);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, TagTableConst.TABLE_TAG);
    }

    @Test
    void addPositive() {
        Tag testTag = new Tag();
        testTag.setName("name");
        try{
            tagDao.add(testTag);
        } catch (Exception e){
            fail(e);
        }
        assertEquals(startRows + 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, TagTableConst.TABLE_TAG));
    }

    @Test
    void addNegative_Exist() {
        Tag testTag = new Tag();
        testTag.setName(EXIST_NAME);
        assertThrows(DuplicateException.class, () -> tagDao.add(testTag));
    }

    @Test
    void findAll() {
        List<Tag> tagList = tagDao.findAll();
        assertEquals(startRows, tagList.size());
    }

    @Test
    void findByNamePositive() {
        Tag testTag = new Tag();
        testTag.setName("Test name");
        try {
            tagDao.add(testTag);
        } catch (DuplicateException e) {
            fail(e);
        }
        Optional<Tag> tagFromDao = tagDao.findByName(testTag.getName());
        assertEquals(testTag.getName(), tagFromDao.get().getName());
    }

    @Test
    void findByNameNegative() {
        Optional<Tag> tagFromDao = tagDao.findByName("not present name");
        assertEquals(Optional.empty(), tagFromDao);
    }

    @Test
    @Disabled   //todo how to reset id auto increment?
    void findById() {
//        Optional<Tag> optionalTag = null;
//        try{
//            optionalTag = tagDao.findById(firstId);
//        } catch (Exception e){
//            fail(e);
//        }
//        if(!optionalTag.isPresent()){
//            fail("Generated id=" + firstId + " wasn't found in DB");
//        }
//        assertEquals(testName, optionalTag.get().getName());
    }

    @Test
    @Disabled   //todo how to reset id auto increment?
    void deleteById() {
        System.out.println(firstId);
        tagDao.deleteById(firstId);
        assertEquals(startRows - 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, TagTableConst.TABLE_TAG));
    }
}