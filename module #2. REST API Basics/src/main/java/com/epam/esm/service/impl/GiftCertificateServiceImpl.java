package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.mapper.GiftCertificateDtoMapper;
import com.epam.esm.service.mapper.TagDtoMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.logging.Logger;

@Validated
@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateDao certificateDao;
    private final GiftCertificateDtoMapper giftMapper;
    private final TagDtoMapper tagMapper;
    private final TagService tagService;

    public GiftCertificateServiceImpl(GiftCertificateDao certificateDao, GiftCertificateDtoMapper giftMapper,
                                      TagService tagService, TagDtoMapper tagMapper){
        this.certificateDao = certificateDao;
        this.giftMapper = giftMapper;
        this.tagService = tagService;
        this.tagMapper = tagMapper;
    }

    @Override
    public long add(@Valid GiftCertificateDto certificateDto){
        GiftCertificate certificate = giftMapper.toEntity(certificateDto);
        List<TagDto> tags = certificateDto.getTags();
        for(TagDto tagDto : tags){
            if(!tagService.doesExist(tagDto)){
                long generatedId = tagService.save(tagDto);
                tagDto.setId(Long.toString(generatedId));
            }
            Tag tag = tagMapper.toEntity(tagDto);
            tag.setId(Long.parseLong(tagDto.getId()));      //todo(set id to tag from tagDto in mapper)
            certificate.addTag(tag);
        }
        return certificateDao.add(certificate);
    }

    @Override
    public GiftCertificateDto getById(long id) {
        GiftCertificate certificate = certificateDao.findById(id);
        List<TagDto> tagDtoList = tagMapper.toDto(certificate.getTagList());
        GiftCertificateDto resultDto = giftMapper.toDto(certificate);
        resultDto.setTags(tagDtoList);
        return resultDto;
    }

    @Override
    public List<GiftCertificateDto> getByCriteria(String tag, String name, String description,
                                                  String sortBy, String order) {
        String criteriaSet = defineCriteriaSet(tag, name, description);
        List<GiftCertificate> certificateList = certificateDao.findByCriteria(criteriaSet, tag, name, description);
        List<GiftCertificateDto> dtoList = giftMapper.toDto(certificateList);
        for(GiftCertificateDto certificateDto : dtoList){
            List<TagDto> tagDtoList = tagService.getByGiftCertificateId(Long.parseLong(certificateDto.getId()));
            certificateDto.setTags(tagDtoList);
        }
        return dtoList;
    }

    private String defineCriteriaSet(String tag, String name, String description){
        String criteriaSet = "";
        if(tag != null && !tag.isEmpty()){
            criteriaSet = criteriaSet + "Tag";
        }
        if(name != null && !name.isEmpty()){
            criteriaSet = criteriaSet + "Name";
        }
        if(description != null && !description.isEmpty()){
            criteriaSet = criteriaSet + "Description";
        }
        String result;
        switch (criteriaSet){
            case GiftCertificateDao.BY_TAG:
                result = GiftCertificateDao.BY_TAG;
                break;
            case GiftCertificateDao.BY_TAG_AND_NAME:
                result = GiftCertificateDao.BY_TAG_AND_NAME;
                break;
            case GiftCertificateDao.BY_NAME:
                result = GiftCertificateDao.BY_NAME;
                break;
            case GiftCertificateDao.BY_TAG_AND_DESCRIPTION:
                result = GiftCertificateDao.BY_TAG_AND_DESCRIPTION;
                break;
            case GiftCertificateDao.BY_DESCRIPTION:
                result = GiftCertificateDao.BY_DESCRIPTION;
                break;
            case GiftCertificateDao.BY_TAG_AND_NAME_AND_DESCRIPTION:
                result = GiftCertificateDao.BY_TAG_AND_NAME_AND_DESCRIPTION;
                break;
            case GiftCertificateDao.BY_NAME_AND_DESCRIPTION:
                result = GiftCertificateDao.BY_NAME_AND_DESCRIPTION;
                break;
            default:
                result = GiftCertificateDao.NO_CRITERIA;
                break;
        }
        return result;
    }

    @Override
    public boolean update(@Valid GiftCertificateDto certificateDto, long certificateId) {
        GiftCertificateDto originalCertDto = getById(certificateId);
        List<Long> deletedTagsId = collectDeletedTagsId(originalCertDto.getTags(), certificateDto.getTags());
        List<Long> addedTagsId = collectAddedTagsId(originalCertDto.getTags(), certificateDto.getTags());
        GiftCertificate updatedCertificate = prepareUpdatedCertificate(certificateDto, originalCertDto);
        updatedCertificate.setId(certificateId);
        return certificateDao.update(updatedCertificate, addedTagsId, deletedTagsId);
    }

    @Override
    public boolean delete(long id) {
        return certificateDao.delete(id);
    }

    private List<Long> collectDeletedTagsId(List<TagDto> originalTagDtoList, List<TagDto> updatedTagDtoList){
        List<Long> deletedTagsId = new ArrayList<>();
        for(TagDto originalTagDto : originalTagDtoList){
            if(!updatedTagDtoList.contains(originalTagDto)){
                deletedTagsId.add(Long.parseLong(originalTagDto.getId()));
            }
        }
        return deletedTagsId;
    }

    private List<Long> collectAddedTagsId(List<TagDto> originalTagDtoList, List<TagDto> updatedTagDtoList) {
        List<Long> addedTagsId = new ArrayList<>();
        for(TagDto updatedTagDto : updatedTagDtoList) {
            if (!tagService.doesExist(updatedTagDto)) {
                long generatedId = tagService.save(updatedTagDto);
                updatedTagDto.setId(Long.toString(generatedId));
            }
            if (!originalTagDtoList.contains(updatedTagDto)) {
                addedTagsId.add(Long.parseLong(updatedTagDto.getId()));
            }
        }
        return addedTagsId;
    }

    private GiftCertificate prepareUpdatedCertificate(GiftCertificateDto updatedDto,
                                                      GiftCertificateDto originalDto){
        if(!updatedDto.getName().equals(originalDto.getName())){
            originalDto.setName(updatedDto.getName());
        }
        if(!updatedDto.getDescription().equals(originalDto.getDescription())){
            originalDto.setDescription(updatedDto.getDescription());
        }
        if(!updatedDto.getPrice().equals(originalDto.getPrice())){
            originalDto.setPrice(updatedDto.getPrice());
        }
        if(!updatedDto.getDuration().equals(originalDto.getDuration())){
            originalDto.setDuration(updatedDto.getDuration());
        }
        if(!updatedDto.getCreateDate().equals(originalDto.getCreateDate())){
            originalDto.setCreateDate(updatedDto.getCreateDate());
        }
        if(!updatedDto.getLastUpdateDate().equals(originalDto.getLastUpdateDate())){
            originalDto.setLastUpdateDate(updatedDto.getLastUpdateDate());
        }
        return giftMapper.toEntity(originalDto);
    }
}
