package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.DuplicateException;
import com.epam.esm.exception.InvalidEntityException;

import javax.validation.Valid;
import java.util.List;

public interface TagService {
    long save(@Valid TagDto tagDto) throws DuplicateException;
    List<TagDto> getAll();
    TagDto getById(long id);
    TagDto getByName(String name) throws InvalidEntityException;
    boolean delete(long id);
}
