package com.epam.esm.dao.criteria.impl;

import com.epam.esm.dao.criteria.GiftCertificateSelector;
import com.epam.esm.dao.mapper.GiftCertificateMapper;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class TagSelector implements GiftCertificateSelector {
    private static final String SELECT_BY_TAG = "SELECT DISTINCT giftcertificate.id, giftcertificate.name, description, " +
            "price, create_date, last_update_date, duration_days " +
            "FROM giftcertificate " +
            "JOIN tag_certificate ON tag_certificate.certificate_id = giftcertificate.id " +
            "JOIN tag ON tag.id = tag_certificate.tag_id " +
            "WHERE LOWER(tag.name) LIKE LOWER(?)";

    @Override
    public List<GiftCertificate> select(String tagName, String name, String description, JdbcTemplate jdbcTemplate,
                                        GiftCertificateMapper giftMapper) {
        tagName = addPercentageWildcard(tagName);
        return jdbcTemplate.query(SELECT_BY_TAG, giftMapper, tagName);
    }
}
