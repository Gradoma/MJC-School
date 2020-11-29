package com.epam.esm.dto;

import com.epam.esm.service.sorting.SortingOrder;
import com.epam.esm.service.sorting.CertificateSortingCriteria;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CertificateCriteria {
    private List<String> tags;
    private String name;
    private String description;
    private CertificateSortingCriteria criteria = CertificateSortingCriteria.DATE;
    private SortingOrder sortingOrder = SortingOrder.DESC;
    private Integer limit;
    private String offset;
}
