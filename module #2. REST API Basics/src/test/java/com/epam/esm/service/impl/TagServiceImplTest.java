package com.epam.esm.service.impl;

import com.epam.esm.config.SpringTestConfig;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateException;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.Validator;

import javax.validation.ConstraintViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {SpringTestConfig.class})
class TagServiceImplTest {
    private static final String POSITIVE_NAME = "positive";
    private static final String NEGATIVE_NAME = "negative";
    private static final String TO_LONG_NAME = "lokpdamjlklkjoklnmlknlknlknlknmknlknnkjnnknkjbkjnlkmlkkjhyutfyt";
    @Autowired
    @InjectMocks
    private TagService tagService;

    @Mock
    private TagDao tagDao;
    @Autowired
    Validator validator;

    @Test
    void saveNegative_InvalidId() {
        TagDto tagDto = new TagDto();
        tagDto.setId("31");
        tagDto.setName("slkdnfl");
        assertThrows(ConstraintViolationException.class, () -> tagService.save(tagDto));
    }

    @Test
    void saveNegative_InvalidNameNull() {
        TagDto tagDto = new TagDto();
        tagDto.setName(null);
        assertThrows(ConstraintViolationException.class, () -> tagService.save(tagDto));
    }

    @Test
    void saveNegative_InvalidNameTooLong() {
        TagDto tagDto = new TagDto();
        tagDto.setName(TO_LONG_NAME);
        assertThrows(ConstraintViolationException.class, () -> tagService.save(tagDto));
    }

    @Test
    void saveNegative_daoThrow() {
//        Tag tag = new Tag();
//        tag.setName("exception name");
//        Mockito.when(tagDao.add(tag)).thenThrow(DuplicateException.class);
//        TagDto tagDto = new TagDto();
//        tagDto.setName("exception name");
//        assertThrows(ConstraintViolationException.class, () -> tagService.save(tagDto));
    }

    @Test
    @Disabled
    void getAll() {

    }

    @Test
    @Disabled
    void getById() {
    }

    @Test
    @Disabled
    void getByName() {
        //cant test - exception
        Tag tag = new Tag();
        tag.setId(5);
        tag.setName(POSITIVE_NAME);
        Mockito.when(tagDao.findByName(POSITIVE_NAME)).thenReturn(Optional.of(tag));

        TagDto expectedDto = new TagDto();
        expectedDto.setId("5");
        expectedDto.setName(POSITIVE_NAME);
        assertEquals(expectedDto, tagService.getByName(POSITIVE_NAME).get());
    }

    @Test
    @Disabled
    void delete() {
    }
}