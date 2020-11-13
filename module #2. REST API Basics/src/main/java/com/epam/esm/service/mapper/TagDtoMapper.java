package com.epam.esm.service.mapper;

import com.epam.esm.entity.Tag;
import com.epam.esm.dto.TagDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TagDtoMapper {

    public Tag toEntity(TagDto tagDto){
        Tag tag = new Tag();
        tag.setId(Long.parseLong(tagDto.getId()));
        tag.setName(tagDto.getName());
        return tag;
    }

    public TagDto toDto(Tag tag){
        TagDto tagDto = new TagDto();
        tagDto.setId(Long.toString(tag.getId()));
        tagDto.setName(tag.getName());
        return tagDto;
    }

    public List<TagDto> toDto(List<Tag> tagList){
        List<TagDto> resultList = new ArrayList<>();
        tagList.forEach(tag -> resultList.add(toDto(tag)));
        return resultList;
    }
}
