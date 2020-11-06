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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.Validator;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringTestConfig.class})
class TagServiceImplTest {
    @Autowired
    @InjectMocks
    private TagService tagService;
    @Autowired
    Validator validator;
    @Mock
    private TagDao tagDao;

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
        tagDto.setName("nma;skdadkalkdaknaldklkakdnaldskalskmaldknalsdknasldkaldkalskdakdnalkdnpsdjlkmcxlkskfdnlk");
        assertThrows(ConstraintViolationException.class, () -> tagService.save(tagDto));
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
    }

    @Test
    @Disabled
    void delete() {
    }
}