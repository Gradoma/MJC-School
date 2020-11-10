package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
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
            "create_date, last_update_date, duration_days, tag.id, tag.name FROM giftcertificate " +
            "JOIN tag_certificate ON tag_certificate.certificate_id = giftcertificate.id " +
            "JOIN tag ON tag.id = tag_certificate.tag_id ";
    private static final String SELECT_BY_ID = "SELECT giftcertificate.id, giftcertificate.name, description, price, " +
            "create_date, last_update_date, duration_days, tag.id, tag.name FROM giftcertificate " +
            "JOIN tag_certificate ON tag_certificate.certificate_id = giftcertificate.id " +
            "JOIN tag ON tag.id = tag_certificate.tag_id " +
            "WHERE giftcertificate.id = ?";
    private static final String SELECT_BY_NAME = "SELECT id, name, description, price, create_date, last_update_date, " +
            "duration_days FROM giftcertificate WHERE LOWER(name) LIKE LOWER(?)";
    private static final String SELECT_BY_DESCRIPTION = "SELECT id, name, description, price, create_date, " +
            "last_update_date, duration_days FROM giftcertificate WHERE LOWER(description) LIKE LOWER(?)";
    private static final String UPDATE = "UPDATE giftcertificate SET name = ?, description = ?, price = ?, " +
            "last_update_date = ?, duration_days = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM giftcertificate WHERE id = ?";
    private static final String TAG_CERT_INSERT = "INSERT INTO tag_certificate (tag_id, certificate_id) VALUES (?, ?)";
    private static final String TAG_CERT_SELECT_BY_TAG_ID = "SELECT certificate_id FROM tag_certificate WHERE tag_id = ?";
    private static final String TAG_CERT_SELECT_BY_CERTIFICATE_ID = "SELECT tag_id FROM tag_certificate " +
            "WHERE certificate_id = ?";
    private static final String TAG_CERT_DELETE = "DELETE FROM tag_certificate WHERE certificate_id = ?";
    private static final String TAG_CERT_DELETE_BY_TAG_AND_CERT_ID = "DELETE FROM tag_certificate WHERE " +
            "tag_id = ? AND certificate_id = ?";
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
    public List<GiftCertificate> findAllWithTags(){
        List<GiftCertificate> certificateList = jdbcTemplate.query(SELECT_ALL, giftMapper);
        for(GiftCertificate certificate : certificateList){
            certificate.setTagList(collectTagList(certificate.getId()));
        }
        return certificateList;
    }

    @Override
    public List<GiftCertificate> findAll(){
        return jdbcTemplate.query(SELECT_ALL, giftMapper);
    }

    @Override
    public List<GiftCertificate> findByTag(long tagId){
        List<GiftCertificate> certificates = new ArrayList<>();
        List<Long> certificatesId = findByTagId(tagId);
        for(Long certId : certificatesId){
            certificates.add(findById(certId));
        }
        return certificates;
    }

    @Override
    public GiftCertificate findById(long id) {
        try{
            GiftCertificate certificate = jdbcTemplate.queryForObject(SELECT_BY_ID, giftMapper, id);
            return certificate;
        } catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Gift certificate: id=" + id);
        }
    }

    @Override
    public List<GiftCertificate> findByName(String name) {
        name = "%" + name.trim() + "%";
        List<GiftCertificate> certificateList = jdbcTemplate.query(SELECT_BY_NAME, giftMapper, name);
        for(GiftCertificate certificate : certificateList){
            certificate.setTagList(collectTagList(certificate.getId()));
        }
        return certificateList;
    }

    @Override
    public List<GiftCertificate> findByDescription(String description) {
        description = "%" + description.trim() + "%";
        List<GiftCertificate> certificateList = jdbcTemplate.query(SELECT_BY_DESCRIPTION, giftMapper, description);
        for(GiftCertificate certificate : certificateList){
            certificate.setTagList(collectTagList(certificate.getId()));
        }
        return certificateList;
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
        return rows > 0;
    }

    private List<Tag> collectTagList(long certificateId){
        List<Tag> tagList = new ArrayList<>();
        List<Long> listTagId = findByCertificateId(certificateId);
        for(Long tagId : listTagId){
            Tag tag = tagDao.findById(tagId);
            tagList.add(tag);
        }
        return tagList;
    }

    private <T> GiftCertificate selectByParameter(String sql, T param){
        GiftCertificate certificate = jdbcTemplate.queryForObject(sql, giftMapper, param);
        List<Long> listTagId = findByCertificateId(certificate.getId());
        for(Long tagId : listTagId){
            Tag tag = tagDao.findById(tagId);
            certificate.addTag(tag);
        }
        return certificate;
    }

    private void addTagToCertId(long tagId, long certificateId){
        jdbcTemplate.update(TAG_CERT_INSERT, tagId, certificateId);
    }

    private List<Long> findByTagId(long tagId) {
        return jdbcTemplate.queryForList(TAG_CERT_SELECT_BY_TAG_ID, Long.class, tagId);
    }

    private List<Long> findByCertificateId(long certificateId) {
        return jdbcTemplate.queryForList(TAG_CERT_SELECT_BY_CERTIFICATE_ID, Long.class, certificateId);
    }

    private boolean deleteTagToCert(long certificateId, long tagId) {
        int rows = jdbcTemplate.update(TAG_CERT_DELETE_BY_TAG_AND_CERT_ID, tagId, certificateId);
        return rows == 1;
    }

    private boolean deleteAllTagsByCertificateId(long certificateId) {
        int rows = jdbcTemplate.update(TAG_CERT_DELETE, certificateId);
        return rows == 1;
    }

    private LocalDateTime convertToUtcLocalDateTime(ZonedDateTime zonedDateTime){
        return zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }
}
