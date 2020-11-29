package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.mapper.TagDtoMapper;
import com.epam.esm.service.sorting.Order;
import com.epam.esm.service.sorting.TagSortingCriteria;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Service
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;
    private final TagDtoMapper dtoMapper;

    public TagServiceImpl(TagDao tagDao, TagDtoMapper dtoMapper){
        this.tagDao = tagDao;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public long save(TagDto tagDto) {
        tagDto.setId(0L);
        Tag tag = dtoMapper.toEntity(tagDto);
        return tagDao.add(tag);
    }

    @Override
    public List<TagDto> getAll(String offset, Integer limit) {
        if(limit == null){
            limit = 5;
        }
        TagSortingCriteria defaultSorting = TagSortingCriteria.ID;
        Order defaultOrder = Order.ASC;
        List<Tag> tagList = tagDao.findAll(defaultSorting, defaultOrder, offset, limit);
        return dtoMapper.toDto(tagList);
    }

    @Override
    public TagDto getById(long id) {
        return dtoMapper.toDto(tagDao.findById(id));
    }

    @Override
    public TagDto getByName(String name) {
        Tag tag = tagDao.findByName(name);
        return dtoMapper.toDto(tag);
    }

    @Override
    public TagDto getMostPopular() {
        Tag tag = tagDao.findMostPopular();
        return dtoMapper.toDto(tag);
    }

    @Override
    public List<TagDto> getByGiftCertificateId(long certificateId) {
        List<Tag> tagList = tagDao.findByCertificateId(certificateId);
        return dtoMapper.toDto(tagList);
    }

    @Override
    public boolean delete(long id) {
        return tagDao.deleteById(id);
    }

    @Override
    public boolean doesExist(TagDto tagDto) {
        Tag tag = dtoMapper.toEntity(tagDto);
        return tagDao.contains(tag);
    }
}
