package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.DuplicateException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.mapper.TagDtoMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Validated
@Service
public class TagServiceImpl implements TagService {
    private static final int NAME_LENGTH = 20;
    private final TagDao tagDao;
    private final TagDtoMapper dtoMapper;

    public TagServiceImpl(TagDao tagDao, TagDtoMapper dtoMapper){
        this.tagDao = tagDao;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public long save(@Valid TagDto tagDto) throws DuplicateException {
        Tag tag = dtoMapper.toEntity(tagDto);
        return tagDao.add(tag);
    }

    @Override
    public List<TagDto> getAll() {
        List<TagDto> resultList = new ArrayList<>();
        List<Tag> tagList = tagDao.findAll();
        for(Tag tag : tagList){
            resultList.add(dtoMapper.toDto(tag));
        }
        return resultList;
    }

    @Override
    public TagDto getById(long id) {
        return dtoMapper.toDto(tagDao.findById(id));
    }

    @Override
    public Optional<TagDto> getByName(@NotNull String name) {
        Optional<Tag> optionalTag = tagDao.findByName(name);
        return optionalTag.map(tag -> dtoMapper.toDto(tag));
//        if(optionalTag.isPresent()){
//            Tag tag = optionalTag.get();
//            return Optional.of(dtoMapper.toDto(tag));
//        } else {
//            return Optional.empty();
//        }
    }

    @Override
    public boolean delete(long id) {
        return tagDao.deleteById(id);
    }

    private boolean isValid(TagDto tagDto){
        if(tagDto.getId() != null ){
            return false;
        }
        return tagDto.getName() != null && tagDto.getName().length() <= NAME_LENGTH;
    }
}
