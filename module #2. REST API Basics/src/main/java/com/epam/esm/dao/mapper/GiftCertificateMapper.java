package com.epam.esm.dao.mapper;

import static com.epam.esm.dao.column.GiftCertificateConst.*;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;

public class GiftCertificateMapper implements RowMapper<GiftCertificate> {

    @Override
    public GiftCertificate mapRow(ResultSet resultSet, int i) throws SQLException {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(resultSet.getLong(ID));
        certificate.setName(resultSet.getString(NAME));
        certificate.setDescription(resultSet.getString(DESCRIPTION));
        certificate.setPrice(resultSet.getDouble(PRICE));
        long createDateMillisUTC = resultSet.getLong(CREATE_DATE);
        certificate.setCreateDate(convertToCurrentZonedDateTime(createDateMillisUTC));
        long lastUpdateMillisUTC = resultSet.getLong(LAST_UPDATE_DATE);
        certificate.setLastUpdateDate(convertToCurrentZonedDateTime(lastUpdateMillisUTC));
        long durationInSeconds = resultSet.getLong(DURATION);
        certificate.setDuration(Duration.ofSeconds(durationInSeconds));
        return certificate;
    }

    private ZonedDateTime convertToCurrentZonedDateTime(long epochMillisUTC){
        Instant instant = Instant.ofEpochMilli(epochMillisUTC);
        return ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
