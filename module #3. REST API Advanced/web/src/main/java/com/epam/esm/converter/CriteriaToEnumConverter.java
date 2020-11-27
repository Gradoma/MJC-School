package com.epam.esm.converter;

import com.epam.esm.service.sorting.SortingCriteria;
import org.springframework.core.convert.converter.Converter;

public class CriteriaToEnumConverter implements Converter<String, SortingCriteria> {
    @Override
    public SortingCriteria convert(String s) {
        return SortingCriteria.valueOf(s.toUpperCase());
    }
}
