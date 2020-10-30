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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringTestConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TagDaoImplTest {
    private static int startRows;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TagDao tagDao;
    private static Tag testTag;

    @BeforeAll
    void beforeAll() {
        ResourceDatabasePopulator tables = new ResourceDatabasePopulator();
        tables.addScript(new ClassPathResource("/db/tag-data.sql"));
        DatabasePopulatorUtils.execute(tables, jdbcTemplate.getDataSource());
        startRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, TagTableConst.TABLE_TAG);
    }

    @AfterAll
    void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, TagTableConst.TABLE_TAG);
    }

    @Test
    @Order(2)
    void add() {
        int rowsBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, TagTableConst.TABLE_TAG);
        testTag = new Tag();
        testTag.setName("name");
        long generatedId = tagDao.add(testTag);
        testTag.setId(generatedId);
        assertEquals(rowsBefore + 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, TagTableConst.TABLE_TAG));
    }

    @Test
    @Order(1)
    void findAll() {
        List<Tag> tagList = tagDao.findAll();
        assertEquals(startRows, tagList.size());
    }

    @Test
    @Order(3)
    void findByName() {
        Optional<Tag> optionalTag = null;
        try{
            optionalTag = tagDao.findByName(testTag.getName());
        } catch (Exception e){
            fail(e);
        }
        if(!optionalTag.isPresent()){
            fail("Tag with name=" + testTag.getName() + " wasn't found in DB");
        }
        assertEquals(testTag, optionalTag.get());
    }

    @Test
    @Order(4)
    void findById() {
        Optional<Tag> optionalTag = null;
        try{
            optionalTag = tagDao.findById(testTag.getId());
        } catch (Exception e){
            fail(e);
        }
        if(!optionalTag.isPresent()){
            fail("Generated id" + testTag.getId() + " wasn't found in DB");
        }
        assertEquals(testTag, optionalTag.get());
    }

    @Test
    @Order(5)
    void deleteById() {
        int rowsBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, TagTableConst.TABLE_TAG);
        tagDao.deleteById(testTag.getId());
        assertEquals(rowsBefore - 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, TagTableConst.TABLE_TAG));
    }
}