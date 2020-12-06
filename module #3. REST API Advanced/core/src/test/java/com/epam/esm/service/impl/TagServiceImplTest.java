package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.criteria.QueryCriteria;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.sorting.TagSortingCriteria;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
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

    @Test
    void getByGiftCertificateId(){
        long certificateId = 15;

        List<Tag> tagList = new ArrayList<>();
        Tag tag1 = new Tag();
        tag1.setId(1);
        tag1.setName("tag1");
        Tag tag2 = new Tag();
        tag2.setId(2);
        tag2.setName("tag2");
        tagList.add(tag1);
        tagList.add(tag2);

        Mockito.doReturn(tagList).when(tagDao).findByCertificateId(ArgumentMatchers.eq(certificateId));

        tagService.getByGiftCertificateId(certificateId);

        Mockito.verify(tagDao, Mockito.times(1))
                .findByCertificateId(ArgumentMatchers.eq(certificateId));
    }

    @Test
    void getById(){
        long tagId = 15;

        Tag tag = new Tag();
        Mockito.doReturn(tag).when(tagDao).findById(tagId);

        tagService.getById(tagId);

        Mockito.verify(tagDao, Mockito.times(1))
                .findById(ArgumentMatchers.eq(tagId));
    }
}