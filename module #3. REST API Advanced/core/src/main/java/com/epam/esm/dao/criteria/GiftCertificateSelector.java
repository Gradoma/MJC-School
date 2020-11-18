package com.epam.esm.dao.criteria;

import com.epam.esm.dao.mapper.GiftCertificateMapper;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    String RESULT_QUERY = "resultQuery";
    String FIRST_PARAM = "firstParam";
    String SECOND_PARAM = "secondParam";

    List<GiftCertificate> select(String tagName, String name, String description, JdbcTemplate jdbcTemplate,
                                 GiftCertificateMapper giftMapper);

    default String addPercentageWildcard(String param){
        return PERCENTAGE + param.trim() + PERCENTAGE;
    }
    default Map<String, String> prepareQuery(String name, String description){
        Map<String, String> resultMap = new HashMap<>();
        String query = DEFAULT_QUERY;
        int paramCount = 0;
        if(name != null){
            query = query + WHERE + BY_NAME;
            resultMap.put(FIRST_PARAM, addPercentageWildcard(name));
            paramCount += 1;
        }
        if(description != null){
            description = addPercentageWildcard(description);
            if(paramCount == 1){
                query = query + AND + BY_DESCRIPTION;
                resultMap.put(SECOND_PARAM, description);
            } else {
                query = query + WHERE + BY_DESCRIPTION;
                resultMap.put(FIRST_PARAM, description);
            }
            paramCount += 1;
        }
        resultMap.put(RESULT_QUERY, query);
        return resultMap;
    }
}
