package com.epam.esm.dao.criteria.impl;

import com.epam.esm.dao.criteria.GiftCertificateSelector;
import com.epam.esm.dao.mapper.GiftCertificateMapper;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class NameDescriptionSelector implements GiftCertificateSelector {
    private static final String SELECT_BY_NAME_AND_DESCRIPTION = "SELECT giftcertificate.id, giftcertificate.name, " +
            "description, price, create_date, last_update_date, duration_days " +
            "FROM giftcertificate " +
            "WHERE LOWER(giftcertificate.name) LIKE LOWER(?) " +
            "AND LOWER(description) LIKE LOWER(?)";

    @Override
    public List<GiftCertificate> select(String tagName, String name, String description, JdbcTemplate jdbcTemplate,
                                        GiftCertificateMapper giftMapper) {
        name = addPercentageWildcard(name);
        description = addPercentageWildcard(description);
        return jdbcTemplate.query(SELECT_BY_NAME_AND_DESCRIPTION, giftMapper, name, description);
    }
}
