package com.epam.esm.dao.mapper;

import static com.epam.esm.dao.column.GiftCertificateTableConst.*;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;

@Component
public class GiftCertificateMapper implements RowMapper<GiftCertificate> {

    @Override
    public GiftCertificate mapRow(ResultSet resultSet, int i) throws SQLException {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(resultSet.getLong(ID));
        certificate.setName(resultSet.getString(NAME));
        certificate.setDescription(resultSet.getString(DESCRIPTION));
        certificate.setPrice(resultSet.getDouble(PRICE));
        LocalDateTime createLocalDateTime = resultSet.getObject(CREATE_DATE, LocalDateTime.class);
        certificate.setCreateDate(ZonedDateTime.of(createLocalDateTime, ZoneOffset.UTC));
        LocalDateTime updateLocalDateTime = resultSet.getObject(LAST_UPDATE_DATE, LocalDateTime.class);
        certificate.setLastUpdateDate(ZonedDateTime.of(updateLocalDateTime, ZoneOffset.UTC));
//        ZonedDateTime createDate = resultSet.getObject(CREATE_DATE, ZonedDateTime.class);
//        certificate.setCreateDate(createDate.withZoneSameInstant(ZoneOffset.UTC));
//        ZonedDateTime lastUpdateDate = resultSet.getObject(LAST_UPDATE_DATE, ZonedDateTime.class);
//        certificate.setLastUpdateDate(lastUpdateDate.withZoneSameInstant(ZoneOffset.UTC));
        long durationInDays = resultSet.getLong(DURATION);
        certificate.setDuration(Duration.ofDays(durationInDays));
        return certificate;
    }
}
