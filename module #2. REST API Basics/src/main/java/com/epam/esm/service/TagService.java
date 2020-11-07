package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.DuplicateException;
import com.epam.esm.exception.InvalidEntityException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface TagService {
    long save(@Valid TagDto tagDto);
    List<TagDto> getAll();
    TagDto getById(long id);
    Optional<TagDto> getByName(@NotNull String name);
    boolean delete(long id);
}
