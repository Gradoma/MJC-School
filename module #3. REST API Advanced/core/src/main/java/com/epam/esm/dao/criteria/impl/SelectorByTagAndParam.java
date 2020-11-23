package com.epam.esm.dao.criteria.impl;

import com.epam.esm.dao.criteria.GiftCertificateSelector;
import com.epam.esm.dao.mapper.GiftCertificateMapper;
import com.epam.esm.entity.GiftCertificate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class SelectorByTagAndParam implements GiftCertificateSelector {
    private static final Logger log = LogManager.getLogger();
    private static final String AND_CERT_ID_IN = " AND giftcertificate.id IN ";
    private static final String WHERE_CERT_ID_IN = " WHERE giftcertificate.id IN ";
    private static final String OPEN_BRAKET = "(";
    private static final String CLOSE_BRAKET = ")";
    private static final String SELECT_CERTIFICATE_ID_BY_TAG = "SELECT giftcertificate.id FROM giftcertificate " +
            "JOIN tag_certificate ON tag_certificate.certificate_id = giftcertificate.id " +
            "JOIN tag ON tag.id = tag_certificate.tag_id " +
            "WHERE LOWER(tag.name) LIKE LOWER(?)";

    @Override
    public List<GiftCertificate> select(List<String> tagNames, String name, String description, JdbcTemplate jdbcTemplate,
                                        GiftCertificateMapper giftMapper) {
        Map<String, String> queryParamMap = prepareQuery(name, description);
        String query = queryParamMap.get(RESULT_QUERY);
        StringBuilder queryBuilder = new StringBuilder(query);
        List<GiftCertificate> resultList;
        switch (queryParamMap.size() - 1){
            case 1:
                log.debug("case 1");
                log.debug("param: " + queryParamMap.get(FIRST_PARAM));
                queryBuilder.append(AND_CERT_ID_IN);
                query = addSelectByTagQueries(tagNames.size(), queryBuilder).append(SEMI).toString();
                log.debug("query = " + query);
                resultList = jdbcTemplate.query(query, giftMapper, prepareArrayTags(tagNames,
                        queryParamMap.get(FIRST_PARAM)));
                break;
            case 2:
                log.debug("case 2");
                log.debug("param 1: " + queryParamMap.get(FIRST_PARAM));
                log.debug("param 2: " + queryParamMap.get(SECOND_PARAM));
                queryBuilder.append(AND_CERT_ID_IN);
                query = addSelectByTagQueries(tagNames.size(), queryBuilder).append(SEMI).toString();
                log.debug("query = " + query);
                resultList = jdbcTemplate.query(query, giftMapper, prepareArrayTags(tagNames,
                        queryParamMap.get(FIRST_PARAM), queryParamMap.get(SECOND_PARAM)));
                break;
            default:
                log.debug("no addition params");
                queryBuilder.append(WHERE_CERT_ID_IN);
                query = addSelectByTagQueries(tagNames.size(), queryBuilder).append(SEMI).toString();
                log.debug("query = " + query);
                resultList = jdbcTemplate.query(query, giftMapper, prepareArrayTags(tagNames));
                break;
        }
        return resultList;
    }

    private String[] prepareArrayTags(List<String> tagNames, String... params){
        List<String> percentageTags = new ArrayList<>();
        tagNames.forEach(tag -> percentageTags.add(addPercentageWildcard(tag)));
        String[] tagArray = percentageTags.stream().toArray(String[]::new);
        return Stream.concat(Arrays.stream(params), Arrays.stream(tagArray))
                .toArray(String[]::new);
    }

    private StringBuilder addSelectByTagQueries(int tagCount, StringBuilder builder){
        builder.append(OPEN_BRAKET);
        builder.append(SELECT_CERTIFICATE_ID_BY_TAG);
        tagCount -= 1;
        if(tagCount > 0){
            builder.append(AND_CERT_ID_IN);
            builder = addSelectByTagQueries(tagCount, builder);
        }
        builder.append(CLOSE_BRAKET);
        return builder;
    }
}
