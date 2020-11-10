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
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Validated
@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final Logger logger = Logger.getLogger("log");
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
            logger.log(Level.INFO, "tagDto:", tagDto);
            if(!tagService.doesExist(tagDto)){
                logger.log(Level.WARNING, "doesnt exist:", tagDto);
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
    public List<GiftCertificateDto> getAll() {
        return giftMapper.toDto(certificateDao.findAll());
    }

    @Override
    public GiftCertificateDto getById(long id) {
        GiftCertificate certificate = certificateDao.findById(id);
        GiftCertificateDto resultDto = giftMapper.toDto(certificate);
        List<TagDto> tagDtoList = new ArrayList<>();
        for(Tag tag : certificate.getTagList()){
            TagDto tagDto = tagService.getById(tag.getId());
            tagDtoList.add(tagDto);
        }
        resultDto.setTags(tagDtoList);
        return resultDto;
    }

    @Override
    public List<GiftCertificateDto> getByName(String name) { return giftMapper.toDto(certificateDao.findByName(name));
    }

    @Override
    public List<GiftCertificateDto> getByDescription(String description) {
        return giftMapper.toDto(certificateDao.findByDescription(description));
    }

    @Override
    public List<GiftCertificateDto> getByTag(String tagName) throws InvalidEntityException {
//        TagDto tagDto = tagService.getByName(tagName);
//        long tagId = Long.parseLong(tagDto.getId());
//        List<GiftCertificate> certificateList = certificateDao.findByTag(tagId);
//        return dtoMapper.toDto(certificateList);
        return null;
    }

    @Override
    public boolean update(GiftCertificateDto certificateDto, long certificateId)
            throws InvalidEntityException {
//        if(!GiftCertificateValidator.isValid(certificateDto)){
//            throw new InvalidEntityException();
//        }
//        GiftCertificate originalCertificate = certificateDao.findById(certificateId);
//        List<Tag> originalTagList = originalCertificate.getTagList();
//        String[] updatedTagNames = certificateDto.getTags();
//        List<Long> deletedTagsId = collectDeletedTagsId(originalTagList, updatedTagNames);
//        List<Long> addedTagsId = collectAddedTagsId(originalTagList, updatedTagNames);
//        GiftCertificate updatedCertificate = compareAndPrepareUpdatedCertificate(originalCertificate,
//                certificateDto);
//        return certificateDao.update(updatedCertificate, addedTagsId, deletedTagsId);
        return false;
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
            throws InvalidEntityException {
//        List<Long> addedTagsId = new ArrayList<>();
//        for(String tagName : updatedTagNames){
//            Optional<Tag> optionalTag = originalTagList.stream()
//                    .filter(t -> t.getName().equals(tagName))
//                    .findAny();
//            if(!optionalTag.isPresent()){
//                addedTagsId.add(getIdOrAddAndReturnIdByName(tagName));
//            }
//        }
//        return addedTagsId;
        return null;
    }

//    private long getIdOrAddAndReturnIdByName(String name) throws InvalidEntityException {
//        long tagId;
//        try {
//            TagDto tagDto = tagService.getByName(name);
//            tagId = Long.parseLong(tagDto.getId());
//        } catch (EmptyResultDataAccessException e){
//            TagDto tagDto = new TagDto();
//            tagDto.setName(name);
//            tagId = tagService.save(tagDto);
//        }
//        return tagId;
//    }

    private GiftCertificate compareAndPrepareUpdatedCertificate(GiftCertificate originalCertificate,
                                                                GiftCertificateDto certificateDto){
        GiftCertificate updatedCertificate = giftMapper.toEntity(certificateDto);
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

//    private boolean isValid(GiftCertificateDto certificateDto){
//        if(certificateDto.getName() == null || certificateDto.getName().isEmpty() ||
//                certificateDto.getName().trim().isEmpty() || certificateDto.getName().length() > NAME_LENGTH){
//            return false;
//        }
//        if(certificateDto.getDescription() == null || certificateDto.getDescription().isEmpty() ||
//                certificateDto.getDescription().trim().isEmpty() ||
//                certificateDto.getDescription().length() > DESCRIPTION_LENGTH){
//            return false;
//        }
//        Pattern pricePattern = Pattern.compile(PRICE_PATTERN);
//        Matcher priceMatcher = pricePattern.matcher(certificateDto.getPrice());
//        if(!priceMatcher.matches()){
//            return false;
//        }
//        Pattern patternDuration = Pattern.compile(DURATION_DAYS);
//        Matcher matcherDuration = patternDuration.matcher(certificateDto.getDuration());
//        if(!matcherDuration.matches()){
//            return false;
//        }
//        try{
//            ZonedDateTime.parse(certificateDto.getCreateDate());
//            ZonedDateTime.parse(certificateDto.getLastUpdateDate());
//        } catch (DateTimeParseException e){
//            return false;
//        }
//        return true;
//    }
}
