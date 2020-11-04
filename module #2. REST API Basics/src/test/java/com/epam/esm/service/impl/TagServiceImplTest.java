package com.epam.esm.service.impl;

import com.epam.esm.config.SpringTestConfig;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.TagDto;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringTestConfig.class})
class TagServiceImplTest {
    private static TagDto tagDto;
    @Mock
    private TagDao tagDaoMock = Mockito.mock(TagDao.class);
    @Autowired
    @InjectMocks
    private TagService tagService;

    @Test
    void save() {
    }

    @Test
    void getAll() {

    }

    @Test
    void getById() {
    }

    @Test
    void getByName() {
    }

    @Test
    void delete() {
        tagService.delete(300);
        Mockito.verify(tagDaoMock).deleteById(300);
    }
}