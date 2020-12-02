package com.epam.esm.dao.criteria;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QueryCriteria {
    public enum Order{ASC, DESC}
    private String sortingCriteria;
    private Order sortingOrder;
    private int firstResult;
    private int resultLimit;
}
