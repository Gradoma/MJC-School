package com.epam.esm.dao.criteria.impl;

import com.epam.esm.dao.criteria.GiftCertificateSelector;
import com.epam.esm.dao.mapper.GiftCertificateMapper;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public class SelectorByCertificateParam implements GiftCertificateSelector {

    @Override
    public List<GiftCertificate> select(List<String> tagNames, String name, String description, JdbcTemplate jdbcTemplate,
                                        GiftCertificateMapper giftMapper) {
        Map<String, String> queryParamMap = prepareQuery(name, description);
        String query = queryParamMap.get(RESULT_QUERY) + SEMI;
        List<GiftCertificate> resultList;
        switch (queryParamMap.size() - 1){
            case 1:
                resultList = jdbcTemplate.query(query, giftMapper, queryParamMap.get(FIRST_PARAM));
                break;
            case 2:
                resultList = jdbcTemplate.query(query, giftMapper, queryParamMap.get(FIRST_PARAM),
                        queryParamMap.get(SECOND_PARAM));
                break;
            default:
                resultList = jdbcTemplate.query(query, giftMapper);
                break;
        }
        return resultList;
    }
}
