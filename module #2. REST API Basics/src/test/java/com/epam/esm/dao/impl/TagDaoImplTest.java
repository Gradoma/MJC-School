package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.column.TagTableConst;
import com.epam.esm.config.SpringTestConfig;
import com.epam.esm.entity.Tag;
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
    private static int startRows;
    private static long firstId = 1;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TagDao tagDao;
    private static final String testName = "sport";


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
    void add() {
        Tag testTag = new Tag();
        testTag.setName("name");
        long generatedId = tagDao.add(testTag);
        testTag.setId(generatedId);
        assertEquals(startRows + 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, TagTableConst.TABLE_TAG));
    }

    @Test
    void findAll() {
        List<Tag> tagList = tagDao.findAll();
        assertEquals(startRows, tagList.size());
    }

    @Test
    @Disabled
    void findByName() {
//        Optional<Tag> optionalTag = null;
//        try{
//            optionalTag = tagDao.findByName(testName);
//        } catch (Exception e){
//            fail(e);
//        }
//        if(!optionalTag.isPresent()){
//            fail("Tag with name=" + testName + " wasn't found in DB");
//        }
//        assertEquals(testName, optionalTag.get().getName());
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