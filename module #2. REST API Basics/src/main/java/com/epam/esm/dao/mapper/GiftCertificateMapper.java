package com.epam.esm.dao.mapper;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static com.epam.esm.dao.column.GiftCertificateTableConst.*;
import static com.epam.esm.dao.column.GiftCertificateTableConst.DURATION;

@Component
public class GiftCertificateMapper implements RowMapper<GiftCertificate> {

    @Override
    public GiftCertificate mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(resultSet.getLong(TABLE_CERTIFICATE + "." + ID));
        certificate.setName(resultSet.getString(TABLE_CERTIFICATE + "." + NAME));
        certificate.setDescription(resultSet.getString(DESCRIPTION));
        certificate.setPrice(resultSet.getDouble(PRICE));
        LocalDateTime createLocalDateTime = resultSet.getObject(CREATE_DATE, LocalDateTime.class);
        certificate.setCreateDate(ZonedDateTime.of(createLocalDateTime, ZoneOffset.UTC));
        LocalDateTime updateLocalDateTime = resultSet.getObject(LAST_UPDATE_DATE, LocalDateTime.class);
        certificate.setLastUpdateDate(ZonedDateTime.of(updateLocalDateTime, ZoneOffset.UTC));
        long durationInDays = resultSet.getLong(DURATION);
        certificate.setDuration(Duration.ofDays(durationInDays));

        return certificate;
    }
}
