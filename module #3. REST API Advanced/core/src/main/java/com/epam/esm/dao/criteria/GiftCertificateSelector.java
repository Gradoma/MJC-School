package com.epam.esm.dao.criteria;

import com.epam.esm.dao.mapper.GiftCertificateMapper;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public interface GiftCertificateSelector {
    String DEFAULT_QUERY = "SELECT giftcertificate.id, giftcertificate.name, " +
            "description, price, create_date, last_update_date, duration_days " +
            "FROM giftcertificate";
    String BY_NAME = " LOWER(giftcertificate.name) LIKE LOWER(?)";
    String BY_DESCRIPTION = " LOWER(description) LIKE LOWER(?)";
    String PERCENTAGE = "%";
    String AND = " AND";
    String WHERE = " WHERE";
    String SEMI = ";";

    List<GiftCertificate> select(String tagName, String name, String description, JdbcTemplate jdbcTemplate,
                                 GiftCertificateMapper giftMapper);
    default String addPercentageWildcard(String param){
        return PERCENTAGE + param.trim() + PERCENTAGE;
    }
}
