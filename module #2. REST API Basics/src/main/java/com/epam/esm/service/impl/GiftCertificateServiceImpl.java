package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagDto;
import com.epam.esm.exception.InvalidParameterException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.mapper.GiftCertificateDtoMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private static final int NAME_LENGTH = 40;
    private static final int DESCRIPTION_LENGTH = 255;
    private static final String PRICE_PATTERN = "^((\\p{Digit}){1,5}([.]\\d{1,2})?)$";
    private static final String DURATION_DAYS = "\\d+";
    private final GiftCertificateDao certificateDao;
    private final GiftCertificateDtoMapper dtoMapper;
    private final TagService tagService;

    public GiftCertificateServiceImpl(GiftCertificateDao certificateDao, GiftCertificateDtoMapper dtoMapper,
                                      TagService tagService){
        this.certificateDao = certificateDao;
        this.dtoMapper = dtoMapper;
        this.tagService = tagService;
    }

    @Override
    public long add(GiftCertificateDto certificateDto) throws InvalidParameterException{
        if(!isValid(certificateDto)){
            throw new InvalidParameterException();
        }
        GiftCertificate certificate = dtoMapper.toEntity(certificateDto);
        String[] tagNames = certificateDto.getTags();
        for(String name : tagNames){
            Tag tag = new Tag();
            tag.setId(getIdOrAddAndReturnIdByName(name));
            tag.setName(name);
            certificate.addTag(tag);
        }
        return certificateDao.add(certificate);
    }

    @Override
    public List<GiftCertificateDto> getAll() {
        return dtoMapper.toDto(certificateDao.findAll());
    }

    @Override
    public GiftCertificateDto getById(long id) {
        return dtoMapper.toDto(certificateDao.findById(id));
    }

    @Override
    public List<GiftCertificateDto> getByName(String name) {
        return dtoMapper.toDto(certificateDao.findByName(name));
    }

    @Override
    public List<GiftCertificateDto> getByDescription(String description) {
        return dtoMapper.toDto(certificateDao.findByDescription(description));
    }

    @Override
    public List<GiftCertificateDto> getByTag(String tagName) throws InvalidParameterException {
        TagDto tagDto = tagService.getByName(tagName);
        long tagId = Long.parseLong(tagDto.getId());
        List<GiftCertificate> certificateList = certificateDao.findByTag(tagId);
        return dtoMapper.toDto(certificateList);
    }

    @Override
    public boolean update(GiftCertificateDto certificateDto, long certificateId)
            throws InvalidParameterException{
        if(!isValid(certificateDto)){
            throw new InvalidParameterException();
        }
        GiftCertificate originalCertificate = certificateDao.findById(certificateId);
        List<Tag> originalTagList = originalCertificate.getTagList();
        String[] updatedTagNames = certificateDto.getTags();
        List<Long> deletedTagsId = collectDeletedTagsId(originalTagList, updatedTagNames);
        List<Long> addedTagsId = collectAddedTagsId(originalTagList, updatedTagNames);
        GiftCertificate updatedCertificate = compareAndPrepareUpdatedCertificate(originalCertificate,
                certificateDto);
        return certificateDao.update(updatedCertificate, addedTagsId, deletedTagsId);
    }

    @Override
    public boolean delete(long id) {
        return certificateDao.delete(id);
    }

    private List<Long> collectDeletedTagsId(List<Tag> originalTagList, String[] updatedTagNames){
        List<Long> deletedTagsId = new ArrayList<>();
        List<String> updatedTagsNamesList = Arrays.asList(updatedTagNames);
        for(Tag originalTag : originalTagList){
            if(!updatedTagsNamesList.contains(originalTag.getName())){
                deletedTagsId.add(originalTag.getId());
            }
        }
        return deletedTagsId;
    }

    private List<Long> collectAddedTagsId(List<Tag> originalTagList, String[] updatedTagNames)
            throws InvalidParameterException{
        List<Long> addedTagsId = new ArrayList<>();
        for(String tagName : updatedTagNames){
            Optional<Tag> optionalTag = originalTagList.stream()
                    .filter(t -> t.getName().equals(tagName))
                    .findAny();
            if(!optionalTag.isPresent()){
                addedTagsId.add(getIdOrAddAndReturnIdByName(tagName));
            }
        }
        return addedTagsId;
    }

    private long getIdOrAddAndReturnIdByName(String name) throws InvalidParameterException{
        long tagId;
        try {
            TagDto tagDto = tagService.getByName(name);
            tagId = Long.parseLong(tagDto.getId());
        } catch (EmptyResultDataAccessException e){
            TagDto tagDto = new TagDto();
            tagDto.setName(name);
            tagId = tagService.save(tagDto);
        }
        return tagId;
    }

    private GiftCertificate compareAndPrepareUpdatedCertificate(GiftCertificate originalCertificate,
                                                                GiftCertificateDto certificateDto){
        GiftCertificate updatedCertificate = dtoMapper.toEntity(certificateDto);
        if(!updatedCertificate.getName().equals(originalCertificate.getName())){
            originalCertificate.setName(updatedCertificate.getName());
        }
        if(!updatedCertificate.getDescription().equals(originalCertificate.getDescription())){
            originalCertificate.setDescription(updatedCertificate.getDescription());
        }
        if(updatedCertificate.getPrice() != originalCertificate.getPrice()){
            originalCertificate.setPrice(updatedCertificate.getPrice());
        }
        if(!updatedCertificate.getDuration().equals(originalCertificate.getDuration())){
            originalCertificate.setDuration(updatedCertificate.getDuration());
        }
        if(!updatedCertificate.getCreateDate().equals(originalCertificate.getCreateDate())){
            originalCertificate.setCreateDate(updatedCertificate.getCreateDate());
        }
        if(!updatedCertificate.getLastUpdateDate().equals(originalCertificate.getLastUpdateDate())){
            originalCertificate.setLastUpdateDate(updatedCertificate.getLastUpdateDate());
        }
        return originalCertificate;
    }

    private boolean isValid(GiftCertificateDto certificateDto){
        if(certificateDto.getName() == null || certificateDto.getName().isEmpty() ||
                certificateDto.getName().trim().isEmpty() || certificateDto.getName().length() > NAME_LENGTH){
            return false;
        }
        if(certificateDto.getDescription() == null || certificateDto.getDescription().isEmpty() ||
                certificateDto.getDescription().trim().isEmpty() ||
                certificateDto.getDescription().length() > DESCRIPTION_LENGTH){
            return false;
        }
        Pattern pricePattern = Pattern.compile(PRICE_PATTERN);
        Matcher priceMatcher = pricePattern.matcher(certificateDto.getPrice());
        if(!priceMatcher.matches()){
            return false;
        }
        Pattern patternDuration = Pattern.compile(DURATION_DAYS);
        Matcher matcherDuration = patternDuration.matcher(certificateDto.getDuration());
        if(!matcherDuration.matches()){
            return false;
        }
        try{
            ZonedDateTime.parse(certificateDto.getCreateDate());
            ZonedDateTime.parse(certificateDto.getLastUpdateDate());
        } catch (DateTimeParseException e){
            return false;
        }
        return true;
    }
}
