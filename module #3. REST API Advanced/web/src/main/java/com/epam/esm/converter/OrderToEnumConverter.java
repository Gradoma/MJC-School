package com.epam.esm.converter;

import com.epam.esm.service.sorting.SortingOrder;
import org.springframework.core.convert.converter.Converter;

public class OrderToEnumConverter implements Converter<String, SortingOrder> {
    @Override
    public SortingOrder convert(String s) {
        return SortingOrder.valueOf(s.toUpperCase());
    }
}
