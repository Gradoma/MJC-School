package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.criteria.CriteriaSet;
import com.epam.esm.dao.criteria.GiftCertificateSelector;
import com.epam.esm.dao.mapper.GiftCertificateMapper;
import com.epam.esm.entity.GiftCertificate;
import static com.epam.esm.dao.column.GiftCertificateTableConst.*;

import com.epam.esm.exception.ResourceNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {
    private static final String SELECT_BY_ID = "SELECT giftcertificate.id, giftcertificate.name, description, price, " +
            "create_date, last_update_date, duration_days FROM giftcertificate " +
            "WHERE giftcertificate.id = ?";
    private static final String UPDATE = "UPDATE giftcertificate SET name = ?, description = ?, price = ?, " +
            "last_update_date = ?, duration_days = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM giftcertificate WHERE id = ?";
    private static final String TAG_CERT_INSERT = "INSERT INTO tag_certificate (tag_id, certificate_id) VALUES (?, ?)";
    private static final String TAG_CERT_DELETE_BY_TAG_AND_CERT_ID = "DELETE FROM tag_certificate WHERE " +
            "tag_id = ? AND certificate_id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final GiftCertificateMapper giftMapper;

    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate, GiftCertificateMapper giftMapper){
        this.giftMapper = giftMapper;
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName(TABLE_CERTIFICATE)
                .usingGeneratedKeyColumns(ID);
    }
    @Override
    public long add(GiftCertificate certificate) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(NAME, certificate.getName());
        parameters.put(DESCRIPTION, certificate.getDescription());
        parameters.put(PRICE, certificate.getPrice());
        parameters.put(CREATE_DATE, convertToUtcLocalDateTime(certificate.getCreateDate()));
        parameters.put(LAST_UPDATE_DATE, convertToUtcLocalDateTime(certificate.getLastUpdateDate()));
        parameters.put(DURATION, certificate.getDuration().toDays());
        long certificateId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();

        certificate.getTagSet().forEach(tag -> addTagToCertId(tag.getId(), certificateId));
        return certificateId;
    }

    @Override
    public GiftCertificate findById(long id) {
        try{
            return jdbcTemplate.queryForObject(SELECT_BY_ID, giftMapper, id);
        } catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Gift certificate: id=" + id);
        }
    }

    @Override
    public List<GiftCertificate> findByCriteria(String criteriaSet, String tagName, String name,
                                                String description) {
        List<GiftCertificate> resultList = new ArrayList<>();
        GiftCertificateSelector selector = CriteriaSet.getByName(criteriaSet).getSelector();
        resultList = selector.select(tagName, name, description, jdbcTemplate, giftMapper);
        if(resultList.size() == 0){
            throw new ResourceNotFoundException("Gift certificate: name=" + name +
                    ", description=" + description +
                    ", tag=" + tagName);
        }
        return resultList;
    }

    @Override
    public boolean update(GiftCertificate certificate, List<Long> addedTagsId, List<Long> deletedTagsId) {
        addedTagsId.forEach(tagIdToAdd -> addTagToCertId(tagIdToAdd, certificate.getId()));
        deletedTagsId.forEach(tagIdToDelete -> deleteTagToCert(certificate.getId(), tagIdToDelete));
        int rows = jdbcTemplate.update(UPDATE, certificate.getName(), certificate.getDescription(),
                certificate.getPrice(), convertToUtcLocalDateTime(certificate.getLastUpdateDate()),
                certificate.getDuration().toDays(), certificate.getId());
        return rows > 0;
    }

    @Override
    public boolean delete(long id) {
        int rows = jdbcTemplate.update(DELETE_BY_ID, id);
        if (rows > 0){
            return true;
        } else {
            throw new ResourceNotFoundException("Gift certificate: id=" + id);
        }
    }

    private void addTagToCertId(long tagId, long certificateId){
        jdbcTemplate.update(TAG_CERT_INSERT, tagId, certificateId);
    }

    private boolean deleteTagToCert(long certificateId, long tagId) {
        int rows = jdbcTemplate.update(TAG_CERT_DELETE_BY_TAG_AND_CERT_ID, tagId, certificateId);
        return rows == 1;
    }

    private LocalDateTime convertToUtcLocalDateTime(ZonedDateTime zonedDateTime){
        return zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }
}
