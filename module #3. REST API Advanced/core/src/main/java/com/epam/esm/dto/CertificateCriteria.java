package com.epam.esm.dto;

import com.epam.esm.dao.criteria.QueryCriteria;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CertificateCriteria extends QueryCriteria {
    private List<String> tags;
    private String name;
    private String description;
    private int page = 1;

    @Override
    public int getResultLimit() {
        return super.getResultLimit();
    }

    @Override
    public String getSortingCriteria() {
        return super.getSortingCriteria();
    }

    @Override
    public Order getSortingOrder() {
        return super.getSortingOrder();
    }

    @Override
    public int getFirstResult() {
        return super.getFirstResult();
    }

    @Override
    public void setSortingCriteria(String sortingCriteria) {
        super.setSortingCriteria(sortingCriteria);
    }

    @Override
    public void setSortingOrder(Order sortingOrder) {
        super.setSortingOrder(sortingOrder);
    }

    @Override
    public void setFirstResult(int firstResult) {
        super.setFirstResult(firstResult);
    }

    @Override
    public void setResultLimit(int resultLimit) {
        super.setResultLimit(resultLimit);
    }

}
