package com.epam.esm.dao.criteria.impl;

import com.epam.esm.dao.criteria.GiftCertificateSelector;
import com.epam.esm.dao.mapper.GiftCertificateMapper;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class NameSelector implements GiftCertificateSelector {
    private static final String SELECT_BY_NAME = "SELECT giftcertificate.id, giftcertificate.name, description, " +
            "price, create_date, last_update_date, duration_days " +
            "FROM giftcertificate " +
            "WHERE LOWER(giftcertificate.name) LIKE LOWER(?)";

    @Override
    public List<GiftCertificate> select(String tagName, String name, String description, JdbcTemplate jdbcTemplate,
                                        GiftCertificateMapper giftMapper) {
        name = addPercentageWildcard(name);
        return jdbcTemplate.query(SELECT_BY_NAME, giftMapper, name);
    }
}
