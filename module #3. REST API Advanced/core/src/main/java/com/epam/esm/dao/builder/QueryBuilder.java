package com.epam.esm.dao.builder;

import com.epam.esm.dto.CertificateCriteria;

public class QueryBuilder {
    private static final String DEFAULT_QUERY = "SELECT giftcertificate.id, giftcertificate.name, " +
            "description, price, create_date, last_update_date, duration_days " +
            "FROM giftcertificate";
    private static final String SELECT_ID_BY_TAGS = " giftcertificate.id IN (SELECT certificate_id FROM tag_certificate " +
            "INNER JOIN tag ON tag_certificate.tag_id = tag.id WHERE tag.name IN (:tag_names)) ";
    private static final String BY_NAME = " LOWER(giftcertificate.name) LIKE LOWER(:name)";
    private static final String BY_DESCRIPTION = " LOWER(description) LIKE LOWER(:description)";
    private static final String AND = " AND";
    private static final String WHERE = " WHERE";
    private static final String ORDER_BY = " ORDER BY ";
    private static final String SPACE = " ";
    private static final String SEMI = ";";

    private static final String BY_NAME_PLACEHOLDER = " LOWER(giftcertificate.name) LIKE LOWER(?)";
    private static final String BY_DESCRIPTION_PLACEHOLDER = " LOWER(description) LIKE LOWER(?)";
    private static final String GIFT_CERT_ID_IN = " giftcertificate.id IN ";
    private static final String OPEN_BRACKET = "(";
    private static final String CLOSE_BRACKET = ")";
    private static final String SELECT_TAG_BY_NAME = "SELECT certificate_id FROM tag_certificate " +
            "INNER JOIN tag ON tag_certificate.tag_id = tag.id " +
            "WHERE LOWER(tag.name) LIKE LOWER(?)";
    private static final String AND_CERT_ID_IN = " AND certificate_id IN ";

    public static String makeQuery(CertificateCriteria criteria){
        int counter = 0;
        StringBuilder builder = new StringBuilder(DEFAULT_QUERY);
        if(criteria.getTags() != null){
            builder.append(WHERE);
            builder.append(SELECT_ID_BY_TAGS);
            counter += 1;
        }
        if(criteria.getName() != null){
            if(counter == 0){
                builder.append(WHERE);
            } else {
                builder.append(AND);
            }
            builder.append(BY_NAME);
            counter += 1;
        }
        if(criteria.getDescription() != null){
            if(counter == 0){
                builder.append(WHERE);
            } else {
                builder.append(AND);
            }
            builder.append(BY_DESCRIPTION);
        }
        builder.append(ORDER_BY);
        builder.append(criteria.getCriteria().getColumn());
        builder.append(SPACE);
        builder.append(criteria.getOrder().toString());
        builder.append(SEMI);
        return builder.toString();
    }

    public static String makeQueryAndTagCondition(CertificateCriteria criteria){
        int counter = 0;
        StringBuilder builder = new StringBuilder(DEFAULT_QUERY);
        if(criteria.getName() != null){
            builder.append(WHERE);
            builder.append(BY_NAME_PLACEHOLDER);
            counter += 1;
        }
        if(criteria.getDescription() != null){
            if(counter == 0){
                builder.append(WHERE);
            } else {
                builder.append(AND);
            }
            builder.append(BY_DESCRIPTION_PLACEHOLDER);
            counter += 1;
        }
        if(criteria.getTags() != null){
            if(counter == 0){
                builder.append(WHERE);
            } else {
                builder.append(AND);
            }
            builder.append(GIFT_CERT_ID_IN);
            addSelectForEveryTag(builder, criteria.getTags().size());
        }
        builder.append(ORDER_BY);
        builder.append(criteria.getCriteria().getColumn());
        builder.append(SPACE);
        builder.append(criteria.getOrder().toString());
        builder.append(SEMI);
        return builder.toString();
    }

    private static StringBuilder addSelectForEveryTag(StringBuilder builder, int tagCount){
        builder.append(OPEN_BRACKET);
        builder.append(SELECT_TAG_BY_NAME);
        tagCount -= 1;
        if(tagCount > 0){
            builder.append(AND_CERT_ID_IN);
            addSelectForEveryTag(builder, tagCount);
        }
        builder.append(CLOSE_BRACKET);
        return builder;
    }
}
