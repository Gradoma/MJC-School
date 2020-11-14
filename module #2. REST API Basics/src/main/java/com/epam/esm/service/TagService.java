package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public interface TagService {
    long save(TagDto tagDto);
    List<TagDto> getAll();
    TagDto getById(long id);
    TagDto getByName(String name);
    List<TagDto> getByGiftCertificateId(long certificateId);
    boolean delete(long id);
    boolean doesExist(TagDto tagDto);
}
