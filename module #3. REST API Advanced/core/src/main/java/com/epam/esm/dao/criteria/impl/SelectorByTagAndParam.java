package com.epam.esm.dao.criteria.impl;

import com.epam.esm.dao.criteria.GiftCertificateSelector;
import com.epam.esm.dao.mapper.GiftCertificateMapper;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public class SelectorByTagAndParam implements GiftCertificateSelector {
    private static final String AND_CERT_ID_IN = " AND giftcertificate.id IN ";
    private static final String SELECT_CERTIFICATE = "SELECT DISTINCT giftcertificate.id FROM giftcertificate ";
    private static final String JOIN = " JOIN tag_certificate ON tag_certificate.certificate_id = giftcertificate.id " +
            "JOIN tag ON tag.id = tag_certificate.tag_id " +
            "WHERE LOWER(tag.name) LIKE LOWER(?)";
    private static final String OPEN_BRAKET = "(";
    private static final String CLOSE_BRAKET = ")";

    @Override
    public List<GiftCertificate> select(String tagName, String name, String description, JdbcTemplate jdbcTemplate,
                                        GiftCertificateMapper giftMapper) {
        Map<String, String> queryParamMap = prepareQuery(name, description);
        String query = queryParamMap.get(RESULT_QUERY);
        tagName = addPercentageWildcard(tagName);
        List<GiftCertificate> resultList;
        switch (queryParamMap.size() - 1){
            case 1:
                query = query + AND_CERT_ID_IN + OPEN_BRAKET + SELECT_CERTIFICATE + JOIN + CLOSE_BRAKET + SEMI;
                resultList = jdbcTemplate.query(query, giftMapper, queryParamMap.get(FIRST_PARAM), tagName);
                break;
            case 2:
                query = query + AND_CERT_ID_IN + OPEN_BRAKET + SELECT_CERTIFICATE + JOIN + CLOSE_BRAKET + SEMI;
                resultList = jdbcTemplate.query(query, giftMapper, queryParamMap.get(FIRST_PARAM),
                        queryParamMap.get(SECOND_PARAM), tagName);
                break;
            default:
                query = query + JOIN + SEMI;
                resultList = jdbcTemplate.query(query, giftMapper, tagName);
                break;
        }
        return resultList;
    }
}
