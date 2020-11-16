package com.epam.esm.dao.criteria;

import com.epam.esm.dao.mapper.GiftCertificateMapper;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public interface GiftCertificateSelector {
    String PERCENTAGE = "%";

    List<GiftCertificate> select(String tagName, String name, String description, JdbcTemplate jdbcTemplate,
                                 GiftCertificateMapper giftMapper);
    default String addPercentageWildcard(String param){
        return PERCENTAGE + param.trim() + PERCENTAGE;
    }
}
