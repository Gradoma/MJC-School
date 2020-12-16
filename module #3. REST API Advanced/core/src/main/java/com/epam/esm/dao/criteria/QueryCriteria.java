package com.epam.esm.dao.criteria;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QueryCriteria {
    public enum Order{ASC, DESC}
    protected String sortingCriteria;
    protected Order sortingOrder;
    protected int firstResult;
    protected int resultLimit;
}
