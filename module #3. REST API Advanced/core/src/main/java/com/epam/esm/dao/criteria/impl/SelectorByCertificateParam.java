package com.epam.esm.dao.criteria.impl;

import com.epam.esm.dao.criteria.GiftCertificateSelector;
import com.epam.esm.dao.mapper.GiftCertificateMapper;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class SelectorByCertificateParam implements GiftCertificateSelector {

    @Override
    public List<GiftCertificate> select(String tagName, String name, String description, JdbcTemplate jdbcTemplate,
                                        GiftCertificateMapper giftMapper) {
        String query = DEFAULT_QUERY;
        boolean wasModified = false;
        if(name != null){
            query = query + WHERE + BY_NAME;
            wasModified = true;
            name = addPercentageWildcard(name);
        }
        if(description != null){
            description = addPercentageWildcard(description);
            if(wasModified){
                query = query + AND + BY_DESCRIPTION;
                return jdbcTemplate.query(query, giftMapper, name, description);
            } else {
                query = query + WHERE + BY_DESCRIPTION;
            }
            query = query + SEMI;
            return jdbcTemplate.query(query, giftMapper, description);
        }
        query = query + SEMI;
        return jdbcTemplate.query(query, giftMapper, name);     //todo (fix default search)
    }
}
