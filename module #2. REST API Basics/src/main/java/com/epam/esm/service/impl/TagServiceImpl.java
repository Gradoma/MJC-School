package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.DuplicateException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.mapper.TagDtoMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public long save(@Valid TagDto tagDto) {
        tagDto.setId("0");
        Tag tag = dtoMapper.toEntity(tagDto);
        return tagDao.add(tag);
    }

    @Override
    public List<TagDto> getAll() {
//        List<TagDto> resultList = new ArrayList<>();
        List<Tag> tagList = tagDao.findAll();       // todo (via list conversion)
        return dtoMapper.toDto(tagList);
//        for(Tag tag : tagList){
//            resultList.add(dtoMapper.toDto(tag));
//        }
//        return resultList;
    }

    @Override
    public TagDto getById(long id) {
        return dtoMapper.toDto(tagDao.findById(id));
    }

    @Override
    public TagDto getByName(@NotNull @Size(min = 1, max = 20) String name) {
        Tag tag = tagDao.findByName(name);
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
    public boolean doesExist(@Valid TagDto tagDto) {
        if(!tagDto.getId().matches("\\d+")){
            return false;
        }
        Tag tag = dtoMapper.toEntity(tagDto);
//        tag.setId(Long.parseLong(tagDto.getId()));      //todo(set id to tag from tagDto in mapper)
        return tagDao.contains(tag);
    }
}
