package com.epam.esm.service.impl;

import com.epam.esm.config.SpringTestConfig;
import com.epam.esm.config.TestApp;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.criteria.QueryCriteria;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.sorting.PaginationUtil;
import com.epam.esm.service.sorting.TagSortingCriteria;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.Validator;

import javax.validation.ConstraintViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApp.class)
class TagServiceImplTest {
    @Autowired
    private TagService tagService;

    @MockBean
    private TagDao tagDao;

    @Test
    void getAll() {
        int page = 1;
        QueryCriteria criteria = new QueryCriteria();
        criteria.setSortingOrder(QueryCriteria.Order.ASC);
        criteria.setSortingCriteria(TagSortingCriteria.ID.getFieldName());
        criteria.setFirstResult(0);
        criteria.setResultLimit(5);

        List<TagDto> resultList = tagService.getAll(page);

        Mockito.verify(tagDao, Mockito.times(1))
                .findAll(ArgumentMatchers.eq(criteria));
    }

    @Test
    void save(){
        TagDto tagDto = new TagDto();
        tagDto.setName("name");

        Tag tag = new Tag();
        tag.setId(0);
        tag.setName("name");

        tagService.save(tagDto);

        Mockito.verify(tagDao, Mockito.times(1))
                .add(ArgumentMatchers.eq(tag));
    }

    @Test
    void saveDuplicate(){
        TagDto tagDto = new TagDto();
        tagDto.setName("duplicate");

        Tag tag = new Tag();
        tag.setId(0);
        tag.setName("duplicate");

        Mockito.doThrow(new DuplicateException()).when(tagDao).add(tag);

        assertThrows(DuplicateException.class, () -> {
            tagService.save(tagDto);
        });

        Mockito.verify(tagDao, Mockito.times(1))
                .add(ArgumentMatchers.eq(tag));
    }
}