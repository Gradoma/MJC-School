package com.epam.esm.dto;

import com.epam.esm.service.sorting.Order;
import com.epam.esm.service.sorting.SortingCriteria;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class CertificateCriteria {
    @NotNull
    private List<String> tags;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private SortingCriteria criteria = SortingCriteria.DATE;
    @NotNull
    private Order order = Order.DESC;

}
