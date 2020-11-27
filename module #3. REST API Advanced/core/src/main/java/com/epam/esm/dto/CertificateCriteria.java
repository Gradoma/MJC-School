package com.epam.esm.dto;

import com.epam.esm.service.sorting.Order;
import com.epam.esm.service.sorting.SortingCriteria;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CertificateCriteria {
    private List<String> tags;
    private String name;
    private String description;
    private SortingCriteria criteria = SortingCriteria.DATE;
    private Order order = Order.DESC;
}
