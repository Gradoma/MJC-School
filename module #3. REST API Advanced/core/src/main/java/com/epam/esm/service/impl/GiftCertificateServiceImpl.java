package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.criteria.QueryCriteria;
import com.epam.esm.dto.CertificateCriteria;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.mapper.GiftCertificateDtoMapper;
import com.epam.esm.service.mapper.TagDtoMapper;
import com.epam.esm.service.sorting.CertificateSortingCriteria;
import com.epam.esm.service.sorting.PaginationUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

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
    @Transactional
    public long add(GiftCertificateDto certificateDto){
        GiftCertificate certificate = giftMapper.toEntity(certificateDto);
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
    public List<GiftCertificateDto> getByCriteria(CertificateCriteria criteria) {
        if(criteria.getSortingCriteria() == null){
            criteria.setSortingCriteria(CertificateSortingCriteria.DATE.getFieldName());
        }
        if(criteria.getSortingOrder() == null){
            criteria.setSortingOrder(QueryCriteria.Order.DESC);
        }
        criteria.setFirstResult(PaginationUtil.startValue(criteria.getPage()));
        criteria.setResultLimit(PaginationUtil.ON_PAGE);
        List<GiftCertificate> certificateList = certificateDao.findByCriteria(criteria);
        List<GiftCertificateDto> dtoList = giftMapper.toDto(certificateList);
        dtoList.forEach(certificateDto -> {
            List<TagDto> tagDtoList = tagService.getByGiftCertificateId(certificateDto.getId());
            certificateDto.setTags(tagDtoList);
        });
        return dtoList;
    }

    @Override
    @Transactional
    public boolean update(GiftCertificateDto certificateDto, long certificateId) {
        GiftCertificateDto originalCertDto = getById(certificateId);
        GiftCertificate updatedCertificate = giftMapper.toEntity(certificateDto);
        certificateDto.getTags().forEach(tagDto -> {
            saveTagIfNew(tagDto);
            Tag tag = tagMapper.toEntity(tagDto);
            updatedCertificate.addTag(tag);
        });
        updatedCertificate.setId(certificateId);
        updatedCertificate.setCreateDate(ZonedDateTime.parse(originalCertDto.getCreateDate()));
        return certificateDao.update(updatedCertificate);
    }

    @Override
    @Transactional
    public boolean patch(GiftCertificateDto certificateDto, long id) {
        GiftCertificate certificateWithPatchField = new GiftCertificate();
        if(certificateDto.getName() != null){
            certificateWithPatchField.setName(certificateDto.getName());
        }
        if(certificateDto.getDescription() != null){
            certificateWithPatchField.setDescription(certificateDto.getDescription());
        }
        if(certificateDto.getDuration() != null){
            long days = certificateDto.getDuration();
            certificateWithPatchField.setDuration(Duration.ofDays(days));
        }
        certificateWithPatchField.setId(id);
        certificateWithPatchField.setLastUpdateDate(ZonedDateTime.now().withZoneSameInstant(ZoneId.systemDefault()));
        return certificateDao.patch(certificateWithPatchField);
    }

    @Override
    @Transactional
    public boolean delete(long id) {
        return certificateDao.delete(id);
    }

    private void saveTagIfNew(TagDto tagDto){
        if (!tagService.doesExist(tagDto)) {
            long generatedId = tagService.save(tagDto);
            tagDto.setId(generatedId);
        }
    }
}
