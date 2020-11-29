package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

import java.util.List;

public interface TagService {
    long save(TagDto tagDto);
    List<TagDto> getAll(String offset, Integer limit);
    TagDto getById(long id);
    TagDto getByName(String name);
    TagDto getMostPopular();
    List<TagDto> getByGiftCertificateId(long certificateId);
    boolean delete(long id);
    boolean doesExist(TagDto tagDto);
}
