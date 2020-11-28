package com.epam.esm.converter;

import com.epam.esm.service.sorting.CertificateSortingCriteria;
import org.springframework.core.convert.converter.Converter;

public class CriteriaToEnumConverter implements Converter<String, CertificateSortingCriteria> {
    @Override
    public CertificateSortingCriteria convert(String s) {
        return CertificateSortingCriteria.valueOf(s.toUpperCase());
    }
}
