package com.epam.esm.dao.mapper;

import static com.epam.esm.dao.column.GiftCertificateTableConst.*;

import com.epam.esm.dao.column.TagTableConst;
import com.epam.esm.dao.column.TagToCertificateTableConst;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class GiftCertificateMapper implements RowMapper<GiftCertificate> {

    @Override
    public GiftCertificate mapRow(ResultSet resultSet, int i) throws SQLException {
        //todo (revert to separated cert mapping and fill it tags
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

        do{
            Tag tag = new Tag();
            tag.setId(resultSet.getLong(TagTableConst.TABLE_TAG + "." + TagTableConst.ID));
            tag.setName(resultSet.getString(TagTableConst.TABLE_TAG + "." + TagTableConst.NAME));
            certificate.addTag(tag);
        } while (resultSet.next());

        return certificate;
    }
}
