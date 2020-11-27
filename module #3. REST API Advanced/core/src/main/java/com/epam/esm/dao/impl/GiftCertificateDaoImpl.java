package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.builder.QueryBuilder;
import com.epam.esm.dao.mapper.GiftCertificateMapper;
import com.epam.esm.dto.CertificateCriteria;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ResourceNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.esm.dao.column.GiftCertificateTableConst.*;

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
    private static final String PERCENTAGE = "%";

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final GiftCertificateMapper giftMapper;

    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate, GiftCertificateMapper giftMapper,
                                  NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.giftMapper = giftMapper;
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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
    public List<GiftCertificate> findByCriteria(CertificateCriteria criteria) {
        List<GiftCertificate> resultList = new ArrayList<>();
        String query = QueryBuilder.makeQuery(criteria);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        if(criteria.getTags() != null){
            parameters.addValue("tag_names", criteria.getTags());
        }
        if(criteria.getName() != null){
            parameters.addValue("name", addPercentageWildcard(criteria.getName()));
        }
        if(criteria.getDescription() != null){
            parameters.addValue("description", addPercentageWildcard(criteria.getDescription()));
        }
        resultList = namedParameterJdbcTemplate.query(query,
                parameters, giftMapper);
        if(resultList.size() == 0){
            throw new ResourceNotFoundException("Gift certificate: name=" + criteria.getName() +
                    ", description=" + criteria.getDescription() +
                    ", tags=" + criteria.getTags());
        }
        return resultList;
    }

    @Override
    public List<GiftCertificate> findByCriteriaAndCondition(CertificateCriteria criteria) {
        List<GiftCertificate> resultList = new ArrayList<>();
        String query = QueryBuilder.makeQueryAndTagCondition(criteria);
        List<String> queryParams = new ArrayList<>();
        if(criteria.getName() != null){
            queryParams.add(addPercentageWildcard(criteria.getName()));
        }
        if(criteria.getDescription() != null){
            queryParams.add(addPercentageWildcard(criteria.getDescription()));
        }
        if(criteria.getTags() != null){
            queryParams.addAll(criteria.getTags());
        }
        if(queryParams.size() > 0){
            String[] paramsArray = queryParams.stream().toArray(String[]::new);
            resultList = jdbcTemplate.query(query, giftMapper, paramsArray);
        } else {
            resultList = jdbcTemplate.query(query, giftMapper);
        }
        if(resultList.size() == 0){
            throw new ResourceNotFoundException("Gift certificate: name=" + criteria.getName() +
                    ", description=" + criteria.getDescription() +
                    ", tags=" + criteria.getTags());
        }
        return resultList;
    }

    private String addPercentageWildcard(String param){
        return PERCENTAGE + param.trim() + PERCENTAGE;
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
    public boolean patch(GiftCertificate certificate) {
        String query = QueryBuilder.makePatchQuery(certificate);
        System.out.println(query);
        int rows = jdbcTemplate.update(query);
        if (rows > 0){
            return true;
        } else {
            throw new ResourceNotFoundException("Gift certificate: id=" + certificate.getId());
        }
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
