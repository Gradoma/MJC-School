package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.mapper.CertMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.dao.mapper.GiftCertificateMapper;
import static com.epam.esm.dao.column.GiftCertificateTableConst.*;

import com.epam.esm.entity.Tag;
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
    private static final String SELECT_ALL = "SELECT giftcertificate.id, giftcertificate.name, description, price, " +
            "create_date, last_update_date, duration_days FROM giftcertificate";
    private static final String SELECT_BY_ID = "SELECT giftcertificate.id, giftcertificate.name, description, price, " +
            "create_date, last_update_date, duration_days, tag.id, tag.name FROM giftcertificate " +
            "JOIN tag_certificate ON tag_certificate.certificate_id = giftcertificate.id " +
            "JOIN tag ON tag.id = tag_certificate.tag_id " +
            "WHERE giftcertificate.id = ?";
    private static final String SELECT_BY_TAG = "SELECT DISTINCT giftcertificate.id, giftcertificate.name, description, " +
            "price, create_date, last_update_date, duration_days " +
            "FROM giftcertificate " +
            "JOIN tag_certificate ON tag_certificate.certificate_id = giftcertificate.id " +
            "JOIN tag ON tag.id = tag_certificate.tag_id " +
            "WHERE LOWER(tag.name) LIKE LOWER(?)";
    private static final String SELECT_BY_TAG_AND_NAME = "SELECT giftcertificate.id, giftcertificate.name, description, " +
            "price, create_date, last_update_date, duration_days " +
            "FROM giftcertificate " +
            "WHERE LOWER(giftcertificate.name) LIKE LOWER(?) " +
            "AND giftcertificate.id IN " +
            "(SELECT DISTINCT giftcertificate.id FROM giftcertificate " +
            "JOIN tag_certificate ON tag_certificate.certificate_id = giftcertificate.id " +
            "JOIN tag ON tag.id = tag_certificate.tag_id " +
            "WHERE LOWER(tag.name) LIKE LOWER(?))";
    private static final String SELECT_BY_TAG_AND_DESCRIPTION = "SELECT giftcertificate.id, giftcertificate.name, description, " +
            "price, create_date, last_update_date, duration_days " +
            "FROM giftcertificate " +
            "WHERE LOWER(description) LIKE LOWER(?) " +
            "AND giftcertificate.id IN " +
            "(SELECT DISTINCT giftcertificate.id FROM giftcertificate " +
            "JOIN tag_certificate ON tag_certificate.certificate_id = giftcertificate.id " +
            "JOIN tag ON tag.id = tag_certificate.tag_id " +
            "WHERE LOWER(tag.name) LIKE LOWER(?))";
    private static final String SELECT_BY_NAME = "SELECT giftcertificate.id, giftcertificate.name, description, " +
            "price, create_date, last_update_date, duration_days " +
            "FROM giftcertificate " +
            "WHERE LOWER(giftcertificate.name) LIKE LOWER(?)";
    private static final String SELECT_BY_DESCRIPTION = "SELECT giftcertificate.id, giftcertificate.name, description, " +
            "price, create_date, last_update_date, duration_days " +
            "FROM giftcertificate " +
            "WHERE LOWER(description) LIKE LOWER(?)";
    private static final String SELECT_BY_NAME_AND_DESCRIPTION = "SELECT giftcertificate.id, giftcertificate.name, " +
            "description, price, create_date, last_update_date, duration_days " +
            "FROM giftcertificate " +
            "WHERE LOWER(giftcertificate.name) LIKE LOWER(?) " +
            "AND LOWER(description) LIKE LOWER(?)";
    private static final String SELECT_BY_TAG_NAME_DESCRIPTION = "SELECT giftcertificate.id, giftcertificate.name, " +
            "description, price, create_date, last_update_date, duration_days " +
            "FROM giftcertificate " +
            "WHERE LOWER(giftcertificate.name) LIKE LOWER(?) " +
            "AND LOWER(description) LIKE LOWER(?) " +
            "AND giftcertificate.id IN " +
            "(SELECT DISTINCT giftcertificate.id FROM giftcertificate " +
            "JOIN tag_certificate ON tag_certificate.certificate_id = giftcertificate.id " +
            "JOIN tag ON tag.id = tag_certificate.tag_id " +
            "WHERE LOWER(tag.name) LIKE LOWER(?))";


    private static final String UPDATE = "UPDATE giftcertificate SET name = ?, description = ?, price = ?, " +
            "last_update_date = ?, duration_days = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM giftcertificate WHERE id = ?";
    private static final String TAG_CERT_INSERT = "INSERT INTO tag_certificate (tag_id, certificate_id) VALUES (?, ?)";
    private static final String TAG_CERT_DELETE_BY_TAG_AND_CERT_ID = "DELETE FROM tag_certificate WHERE " +
            "tag_id = ? AND certificate_id = ?";

    private static final String PERCENTAGE = "%";
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final TagDao tagDao;
    private final GiftCertificateMapper giftMapper;

    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate, TagDao tagDao, GiftCertificateMapper giftMapper){
        this.giftMapper = giftMapper;
        this.tagDao = tagDao;
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

        for(Tag tag : certificate.getTagList()){
            addTagToCertId(tag.getId(), certificateId);
        }

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
        CertMapper mapper = new CertMapper();
        List<GiftCertificate> resultList = new ArrayList<>();
        switch (criteriaSet){
            case GiftCertificateDao.BY_TAG:
                tagName = addPercentageWildcard(tagName);
                resultList = jdbcTemplate.query(SELECT_BY_TAG, mapper, tagName);
                break;
            case GiftCertificateDao.BY_TAG_AND_NAME:
                tagName = addPercentageWildcard(tagName);
                name = addPercentageWildcard(name);
                resultList = jdbcTemplate.query(SELECT_BY_TAG_AND_NAME, mapper, name, tagName);
                break;
            case GiftCertificateDao.BY_NAME:
                name = addPercentageWildcard(name);
                resultList = jdbcTemplate.query(SELECT_BY_NAME, mapper, name);
                break;
            case GiftCertificateDao.BY_TAG_AND_DESCRIPTION:
                tagName = addPercentageWildcard(tagName);
                description = addPercentageWildcard(description);
                resultList = jdbcTemplate.query(SELECT_BY_TAG_AND_DESCRIPTION, mapper, description, tagName);
                break;
            case GiftCertificateDao.BY_DESCRIPTION:
                description = addPercentageWildcard(description);
                resultList = jdbcTemplate.query(SELECT_BY_DESCRIPTION, mapper, description);
                break;
            case GiftCertificateDao.BY_TAG_AND_NAME_AND_DESCRIPTION:
                tagName = addPercentageWildcard(tagName);
                name = addPercentageWildcard(name);
                description = addPercentageWildcard(description);
                resultList = jdbcTemplate.query(SELECT_BY_TAG_NAME_DESCRIPTION, mapper, name, description, tagName);
                break;
            case GiftCertificateDao.BY_NAME_AND_DESCRIPTION:
                name = addPercentageWildcard(name);
                description = addPercentageWildcard(description);
                resultList = jdbcTemplate.query(SELECT_BY_NAME_AND_DESCRIPTION, mapper, name, description);
                break;
            case GiftCertificateDao.NO_CRITERIA:
                resultList = jdbcTemplate.query(SELECT_ALL, mapper);
                break;
        }
        if(resultList.size() == 0){
            throw new ResourceNotFoundException("Gift certificate: name=" + name +
                    ", description=" + description +
                    ", tag=" + tagName);
        }
        return resultList;
    }

    @Override
    public boolean update(GiftCertificate certificate, List<Long> addedTagsId, List<Long> deletedTagsId) {
        for(long id : addedTagsId){
            addTagToCertId(id, certificate.getId());
        }
        for(long id : deletedTagsId){
            deleteTagToCert(certificate.getId(), id);
        }
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

    private String addPercentageWildcard(String param){
        return PERCENTAGE + param.trim() + PERCENTAGE;
    }
}
