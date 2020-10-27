package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.dao.mapper.GiftCertificateMapper;
import static com.epam.esm.dao.column.GiftCertificateConst.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiftCertificateDaoImpl implements GiftCertificateDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
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
    private static final String DELETE_BY_ID = "DELETE FROM giftcertificate WHERE Id=?";

    @Autowired
    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate){
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
        Number generatedId = simpleJdbcInsert.executeAndReturnKey(parameters);
        return generatedId.longValue();
    }

    @Override
    public List<GiftCertificate> findAll() {
        return jdbcTemplate.query(SELECT_ALL, giftMapper);
    }

    @Override
    public GiftCertificate findById(long id) {
        return jdbcTemplate.queryForObject(SELECT_BY_ID, GiftCertificate.class, id);
    }

    @Override
    public GiftCertificate findByName(String name) {     //TODO (DB function call) + return list
        return jdbcTemplate.queryForObject(SELECT_BY_NAME, GiftCertificate.class, name);
    }

    @Override
    public GiftCertificate findByDescription(String description) {  //TODO (DB function call) + return list
        return jdbcTemplate.queryForObject(SELECT_BY_DESCRIPTION, GiftCertificate.class, description);
    }

    @Override
    public boolean update(GiftCertificate certificate) {
        int rows = jdbcTemplate.update(UPDATE, certificate.getName(), certificate.getDescription(),
                certificate.getPrice(), convertToUtcEpochMillis(certificate.getLastUpdateDate()),
                certificate.getDuration().getSeconds());       //TODO (simplify)
        return rows > 0;
    }

    @Override
    public boolean delete(long id) {
        int rows = jdbcTemplate.update(DELETE_BY_ID, id);
        return rows > 0;
    }

    private long convertToUtcEpochMillis(ZonedDateTime zonedDateTime){
        ZonedDateTime createDateUtc = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC);
        return createDateUtc.toInstant().toEpochMilli();
    }
}
