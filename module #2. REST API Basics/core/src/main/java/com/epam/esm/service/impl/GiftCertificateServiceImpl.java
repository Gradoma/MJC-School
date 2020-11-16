package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.comparator.DateComparator;
import com.epam.esm.service.comparator.NameComparator;
import com.epam.esm.service.mapper.GiftCertificateDtoMapper;
import com.epam.esm.service.mapper.TagDtoMapper;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private static final String TAG = "Tag";
    private static final String NAME = "Name";
    private static final String DESCRIPTION = "Description";
    private static final String ASC = "ASC";
    private static final String SORT_BY_DATE = "DATE";
    private final DateComparator dateComparator = new DateComparator();
    private final NameComparator nameComparator = new NameComparator();
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
    public long add(GiftCertificateDto certificateDto){
        GiftCertificate certificate = giftMapper.toEntity(certificateDto);
        if(certificate.getCreateDate() == null){
            certificate.setCreateDate(ZonedDateTime.now());
            certificate.setLastUpdateDate(certificate.getCreateDate());
        }
        if(certificate.getLastUpdateDate() == null){
            certificate.setLastUpdateDate(ZonedDateTime.now());
        }
        certificateDto.getTags().forEach(tagDto -> {
            saveTagIfNew(tagDto);
            Tag tag = tagMapper.toEntity(tagDto);
            certificate.addTag(tag);
        });
        return certificateDao.add(certificate);
    }

    @Override
    public GiftCertificateDto getById(long id) {
        GiftCertificate certificate = certificateDao.findById(id);
        List<TagDto> tagDtoList = tagService.getByGiftCertificateId(certificate.getId());
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
        dtoList.forEach(certificateDto -> {
            List<TagDto> tagDtoList = tagService.getByGiftCertificateId(Long.parseLong(certificateDto.getId()));
            certificateDto.setTags(tagDtoList);
        });
        return sortResultList(dtoList, sortBy, order);
    }

    @Override
    public boolean update(GiftCertificateDto certificateDto, long certificateId) {
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

    private void saveTagIfNew(TagDto tagDto){
        if (!tagService.doesExist(tagDto)) {
            long generatedId = tagService.save(tagDto);
            tagDto.setId(Long.toString(generatedId));
        }
    }

    private List<Long> collectDeletedTagsId(List<TagDto> originalTagDtoList, List<TagDto> updatedTagDtoList){
        List<Long> deletedTagsId = new ArrayList<>();
        originalTagDtoList.forEach(tagDto -> {
            if(!updatedTagDtoList.contains(tagDto)){
                deletedTagsId.add(Long.parseLong(tagDto.getId()));
            }
        });
        return deletedTagsId;
    }

    private List<Long> collectAddedTagsId(List<TagDto> originalTagDtoList, List<TagDto> updatedTagDtoList) {
        List<Long> addedTagsId = new ArrayList<>();
        updatedTagDtoList.forEach(tagDto -> {
            saveTagIfNew(tagDto);
            if (!originalTagDtoList.contains(tagDto)) {
                addedTagsId.add(Long.parseLong(tagDto.getId()));
            }
        });
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
        GiftCertificate updatedCertificate = giftMapper.toEntity(originalDto);
        updatedCertificate.setLastUpdateDate(ZonedDateTime.now().withZoneSameInstant(ZoneId.systemDefault()));
        return updatedCertificate;
    }

    private String defineCriteriaSet(String tag, String name, String description){
        String criteriaSet = "";
        if(tag != null && !tag.isEmpty()){
            criteriaSet = criteriaSet + TAG;
        }
        if(name != null && !name.isEmpty()){
            criteriaSet = criteriaSet + NAME;
        }
        if(description != null && !description.isEmpty()){
            criteriaSet = criteriaSet + DESCRIPTION;
        }
        return criteriaSet;
    }

    private List<GiftCertificateDto> sortResultList(List<GiftCertificateDto> resultList, String sortBy,
                                                    String order){
        boolean isAscending = false;
        if(ASC.equalsIgnoreCase(order)){
            isAscending = true;
        }
        if (SORT_BY_DATE.equalsIgnoreCase(sortBy)) {
            if (isAscending) {
                resultList.sort(dateComparator);
            } else {
                resultList.sort(dateComparator.reversed());
            }
        } else {
            if (isAscending) {
                resultList.sort(nameComparator);
            } else {
                resultList.sort(nameComparator.reversed());
            }
        }
        return resultList;
    }
}
