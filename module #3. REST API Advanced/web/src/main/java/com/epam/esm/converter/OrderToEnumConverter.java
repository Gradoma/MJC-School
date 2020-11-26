package com.epam.esm.converter;

import com.epam.esm.service.sorting.Order;
import org.springframework.core.convert.converter.Converter;

public class OrderToEnumConverter implements Converter<String, Order> {
    @Override
    public Order convert(String s) {
        return Order.valueOf(s.toUpperCase());
    }
}
