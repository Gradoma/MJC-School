package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagDto;
import com.epam.esm.exception.InvalidParameterException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.mapper.TagDtoMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public long save(TagDto tagDto) throws InvalidParameterException{
        if(!isValid(tagDto)){
            throw new InvalidParameterException("Invalid for saving");
        }
        Tag tag = dtoMapper.toEntity(tagDto);
        try{
            tagDao.findByName(tag.getName());
            throw new InvalidParameterException("Already exist");
        } catch (EmptyResultDataAccessException e){
            return tagDao.add(tag);
        }
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
    public TagDto getByName(String name) throws InvalidParameterException{
        if(name == null){
            throw new InvalidParameterException("Name can't be null");
        }
        return dtoMapper.toDto(tagDao.findByName(name));
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
