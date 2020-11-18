package com.epam.esm.dao.criteria.impl;

import com.epam.esm.dao.criteria.GiftCertificateSelector;
import com.epam.esm.dao.mapper.GiftCertificateMapper;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class SelectorByTag implements GiftCertificateSelector {
    private static final String AND_CERT_ID_IN = " AND giftcertificate.id IN ";
    private static final String SELECT_CERTIFICATE = "(SELECT DISTINCT giftcertificate.id FROM giftcertificate ";
    private static final String JOIN = " JOIN tag_certificate ON tag_certificate.certificate_id = giftcertificate.id " +
            "JOIN tag ON tag.id = tag_certificate.tag_id " +
            "WHERE LOWER(tag.name) LIKE LOWER(?)";

    @Override
    public List<GiftCertificate> select(String tagName, String name, String description, JdbcTemplate jdbcTemplate,
                                        GiftCertificateMapper giftMapper) {
        String query = DEFAULT_QUERY;
        int paramCounter = 0;
        String firstCriteria = null;
        String secondCriteria = null;
        if(name != null){
            query = query + WHERE + BY_NAME;
            firstCriteria = addPercentageWildcard(name);
            paramCounter += 1;
        }
        if(description != null){
            description = addPercentageWildcard(description);
            if(paramCounter == 1){
                secondCriteria = description;
                query = query + AND + BY_DESCRIPTION;
            } else {
                firstCriteria = description;
                query = query + WHERE + BY_DESCRIPTION;
            }
            paramCounter += 1;
        }
        tagName = addPercentageWildcard(tagName);
        List<GiftCertificate> resultList = new ArrayList<>();
        switch (paramCounter){
            case 1:
                query = query + AND_CERT_ID_IN + SELECT_CERTIFICATE + JOIN + ")" + SEMI;
                resultList = jdbcTemplate.query(query, giftMapper, firstCriteria, tagName);
                break;
            case 2:
                query = query + AND_CERT_ID_IN + SELECT_CERTIFICATE + JOIN + ")" + SEMI;
                resultList = jdbcTemplate.query(query, giftMapper, firstCriteria, secondCriteria, tagName);
                break;
            default:
                query = query + JOIN + SEMI;
                resultList = jdbcTemplate.query(query, giftMapper, tagName);
                break;
        }
        return resultList;
    }
}
