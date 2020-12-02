package com.epam.esm.converter;

import com.epam.esm.dao.criteria.QueryCriteria;
import org.springframework.core.convert.converter.Converter;

public class OrderToEnumConverter implements Converter<String, QueryCriteria.Order> {
    @Override
    public QueryCriteria.Order convert(String s) {
        return QueryCriteria.Order.valueOf(s.toUpperCase());
    }
}
