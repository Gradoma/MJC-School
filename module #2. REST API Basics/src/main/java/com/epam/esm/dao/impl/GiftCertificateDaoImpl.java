package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.dao.mapper.GiftCertificateMapper;
import static com.epam.esm.dao.column.GiftCertificateTableConst.*;
import static com.epam.esm.dao.column.TagToCertificateTableConst.CERTIFICATE_ID;
import static com.epam.esm.dao.column.TagToCertificateTableConst.TAG_ID;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {
    private static final GiftCertificateMapper giftMapper = new GiftCertificateMapper();
    private static final String SELECT_ALL = "SELECT Id, Name, Description, Price, CreateDate, LastUpdateDate, " +
            "Duration FROM giftcertificate";
    private static final String SELECT_BY_ID = "SELECT Id, Name, Description, Price, CreateDate, LastUpdateDate, " +
            "Duration FROM giftcertificate WHERE Id=?";
    private static final String SELECT_BY_NAME = "SELECT Id, Name, Description, Price, CreateDate, LastUpdateDate, " +
            "Duration FROM giftcertificate WHERE Name=?";
    private static final String SELECT_BY_DESCRIPTION = "SELECT Id, Name, Description, Price, CreateDate, " +
            "LastUpdateDate, Duration FROM giftcertificate WHERE Description=?";
    private static final String UPDATE = "UPDATE giftcertificate SET Name = ?, Description = ?, Price = ?, " +
            "LastUpdateDate = ?, Duration = ? WHERE Id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM giftcertificate WHERE Id = ?";
    private static final String TAG_CERT_INSERT = "INSERT INTO tag_certificate (TagId, CertificateId) VALUES (?, ?)";
    private static final String TAG_CERT_SELECT_BY_TAG_ID = "SELECT CertificateId FROM tag_certificate WHERE TagId = ?";
    private static final String TAG_CERT_SELECT_BY_CERTIFICATE_ID = "SELECT TagId FROM tag_certificate " +
            "WHERE CertificateId = ?";
    private static final String TAG_CERT_DELETE = "DELETE FROM tag_certificate WHERE CertificateId = ?";
    private static final String TAG_CERT_DELETE_BY_TAG_AND_CERT_ID = "DELETE FROM tag_certificate WHERE " +
            "TagId = ? AND CertificateId = ?";
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final TagDao tagDao;

    @Autowired
    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate, TagDao tagDao){
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
        parameters.put(CREATE_DATE, convertToUtcEpochMillis(certificate.getCreateDate()));
        parameters.put(LAST_UPDATE_DATE, convertToUtcEpochMillis(certificate.getLastUpdateDate()));
        parameters.put(DURATION, certificate.getDuration().getSeconds());
        long certificateId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();

        for(Tag tag : certificate.getTagList()){
            addTagIdCertId(tag.getId(), certificateId);
        }

        return certificateId;
    }

    @Override
    public List<GiftCertificate> findAll() throws DaoException {
        List<GiftCertificate> certificateList = jdbcTemplate.query(SELECT_ALL, giftMapper);
        for(GiftCertificate certificate : certificateList){
            List<Long> listTagId = findByCertificateId(certificate.getId());
            for(Long id : listTagId){
                Optional<Tag> optionalTag = tagDao.findById(id);
                if(optionalTag.isPresent()){
                    certificate.addTag(optionalTag.get());
                }
            }
        }
        return certificateList;
    }

    @Override
    public List<GiftCertificate> findByTag(long tagId) throws DaoException{
        List<GiftCertificate> certificates = new ArrayList<>();
        List<Long> certificatesId = findByTagId(tagId);
        for(Long certId : certificatesId){
            certificates.add(findById(certId).get());
        }
        return certificates;
    }

    @Override
    public Optional<GiftCertificate> findById(long id) throws DaoException{
        return selectByParameter(SELECT_BY_ID, id);
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) throws DaoException{     //TODO (DB function call) + return list
        return selectByParameter(SELECT_BY_NAME, name);
    }

    @Override
    public Optional<GiftCertificate> findByDescription(String description) throws DaoException{  //TODO (DB function call) + return list
        return selectByParameter(SELECT_BY_DESCRIPTION, description);
    }

    @Override
    public boolean update(GiftCertificate certificate) {
        // check in service
//        List<Tag> updatedTagList = certificate.getTagList();
//        List<Tag> originalTagList = new ArrayList<>();
//        for(Long id : findByCertificateId(certificate.getId())){
//            Optional<Tag> optionalTag = tagDao.findById(id);
//            if(optionalTag.isPresent()){
//                originalTagList.add(optionalTag.get());
//            }
//        }
//
//        if(!updatedTagList.containsAll(originalTagList)){
//            for(Tag tag : originalTagList){
//                if(!updatedTagList.contains(tag)){
//                    deleteTagByTagIdCertId(certificate.getId(), tag.getId());
//                }
//            }
//        }
//
//        if(!originalTagList.containsAll(updatedTagList)){
//            for(Tag tag: updatedTagList){
//                if(!originalTagList.contains(tag)){
//                    long tagId = tag.getId();
//                    if(!tagDao.findById(tag.getId()).isPresent()){
//                        tagId = tagDao.add(tag);
//                    }
//                    addTagIdCertId(tagId, certificate.getId());
//                }
//            }
//        }

        int rows = jdbcTemplate.update(UPDATE, certificate.getName(), certificate.getDescription(),
                certificate.getPrice(), convertToUtcEpochMillis(certificate.getLastUpdateDate()),
                certificate.getDuration().getSeconds());    //todo named param?
        return rows > 0;
    }

    @Override
    public boolean delete(long id) {
        int rows = jdbcTemplate.update(DELETE_BY_ID, id);
        return rows > 0;
    }

    private Optional<GiftCertificate> selectByParameter(String sql, Object param) throws DaoException{
        List<GiftCertificate> certificates = jdbcTemplate.query(sql, giftMapper, param);
        if(certificates.size() == 0){
            return Optional.empty();
        } else if (certificates.size() > 1){
            throw new DaoException("Incorrect result count: expected 1, actual " + certificates.size());
        } else {
            GiftCertificate certificate = certificates.get(0);
            List<Long> listTagId = findByCertificateId(certificate.getId());
            for(Long tagId : listTagId){
                Optional<Tag> optionalTag = tagDao.findById(tagId);
                if(optionalTag.isPresent()){
                    certificate.addTag(optionalTag.get());
                }
            }
            return Optional.of(certificate);
        }
    }

    private void addTagIdCertId(long tagId, long certificateId){
        jdbcTemplate.update(TAG_CERT_INSERT, tagId, certificateId);
    }

    private List<Long> findByTagId(long tagId) {
        return jdbcTemplate.query(TAG_CERT_SELECT_BY_TAG_ID, new RowMapper<Long>() {
            @Override
            public Long mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getLong(CERTIFICATE_ID);
            }
        }, tagId);
    }

    private List<Long> findByCertificateId(long certificateId) {     //todo private
        return jdbcTemplate.query(TAG_CERT_SELECT_BY_CERTIFICATE_ID, new RowMapper<Long>() {
            @Override
            public Long mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getLong(TAG_ID);
            }
        }, certificateId);
    }

    private boolean deleteTagByTagIdCertId(long certificateId, long tagId) {
        int rows = jdbcTemplate.update(TAG_CERT_DELETE_BY_TAG_AND_CERT_ID, tagId, certificateId);
        return rows == 1;
    }

    private boolean deleteAllTagsByCertificateId(long certificateId) {
        int rows = jdbcTemplate.update(TAG_CERT_DELETE, certificateId);
        return rows == 1;
    }

    private long convertToUtcEpochMillis(ZonedDateTime zonedDateTime){
        ZonedDateTime createDateUtc = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC);
        return createDateUtc.toInstant().toEpochMilli();
    }
}
