package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagDto;
import com.epam.esm.exception.InvalidParameterException;

import java.util.List;

public interface TagService {
    void save(TagDto tagDto) throws InvalidParameterException;
    List<TagDto> getAll();
    TagDto getById(long id);
    TagDto getByName(String name) throws InvalidParameterException;
    boolean delete(long id);
}
