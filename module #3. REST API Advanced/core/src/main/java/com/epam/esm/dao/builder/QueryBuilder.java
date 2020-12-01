package com.epam.esm.dao.builder;

import com.epam.esm.dao.column.GiftCertificateTableConst;
import com.epam.esm.dto.CertificateCriteria;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.sorting.SortingOrder;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class QueryBuilder {
    private static final String DEFAULT_QUERY = "SELECT giftcertificate.id, giftcertificate.name, " +
            "description, price, create_date, last_update_date, duration_days " +
            "FROM giftcertificate";
    private static final String SELECT_ID_BY_TAGS = " giftcertificate.id IN (SELECT certificate_id FROM tag_certificate " +
            "INNER JOIN tag ON tag_certificate.tag_id = tag.id WHERE tag.name IN (:tag_names)) ";
    private static final String BY_NAME = " LOWER(giftcertificate.name) LIKE LOWER(:name)";
    private static final String BY_DESCRIPTION = " LOWER(description) LIKE LOWER(:description)";
    private static final String AND = " AND";
    private static final String WHERE = " WHERE ";
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
    private static final String UPDATE = "UPDATE giftcertificate SET last_update_date ";
    private static final String WHERE_ID = "' WHERE id = ";
    private static final String LIMIT = " LIMIT ";

    public static String makeQuery(CertificateCriteria criteria){
        int counter = 0;
        StringBuilder builder = new StringBuilder(DEFAULT_QUERY);
        if(criteria.getOffset() != null){
            builder.append(addOffset(criteria.getCriteria().getColumn(), criteria.getOffset(),
                    criteria.getSortingOrder(), true));
            counter += 1;
        }
        if(criteria.getTags() != null){
            if(counter == 0){
                builder.append(WHERE);
            } else {
                builder.append(AND);
            }
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
        builder.append(addSorting(criteria.getCriteria().getColumn(), criteria.getSortingOrder().toString()));
        builder.append(addLimit(criteria.getLimit()));
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
        builder.append(addSorting(criteria.getCriteria().getColumn(), criteria.getSortingOrder().toString()));
        builder.append(addLimit(criteria.getLimit()));
        builder.append(SEMI);
        return builder.toString();
    }

    public static String makePatchQuery(GiftCertificate certificate){
        String equal = "='";
        StringBuilder builder = new StringBuilder(UPDATE);
        builder.append(equal);
        builder.append(convertToUtcLocalDateTime(certificate.getLastUpdateDate()).toString());
        builder.append("'");
        builder.append(", ");
        if(certificate.getName() != null){
            builder.append(GiftCertificateTableConst.NAME);
            builder.append(equal);
            builder.append(certificate.getName());
        }
        if(certificate.getDescription() != null){
            builder.append(GiftCertificateTableConst.DESCRIPTION);
            builder.append(equal);
            builder.append(certificate.getDescription());
        }
        if(certificate.getDuration() != null){
            builder.append(GiftCertificateTableConst.DURATION);
            builder.append(equal);
            builder.append(certificate.getDuration().toDays());
        }
        builder.append(WHERE_ID);
        builder.append(certificate.getId());
        builder.append(SEMI);
        return builder.toString();
    }

    public static String addSorting(String byField, String order){     //ORDER BY t.id DESC
        StringBuilder builder = new StringBuilder();
        builder.append(ORDER_BY);
        builder.append(byField);
        builder.append(SPACE);
        builder.append(order);
        return builder.toString();
    }

    public static String addOffset(String columnName, String offset, SortingOrder sortingOrder, boolean firstCondition){
        StringBuilder builder = new StringBuilder();
        String sign;
        if(sortingOrder == SortingOrder.ASC){
            sign = ">'";
        } else {
            sign = "<'";
        }
        if (firstCondition) {
            builder.append(WHERE);
        } else {
            builder.append(AND);
            builder.append(SPACE);
        }
        builder.append(columnName);
        builder.append(sign);
        builder.append(offset);
        return builder.append("'").toString();
    }

    public static String addLimit(int limit){
        return LIMIT + limit + SEMI;
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

    private static LocalDateTime convertToUtcLocalDateTime(ZonedDateTime zonedDateTime){
        return zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }
}
