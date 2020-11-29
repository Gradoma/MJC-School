package com.epam.esm.dao;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.service.sorting.OrderSortingCriteria;
import com.epam.esm.service.sorting.SortingOrder;

import java.util.List;

public interface OrderDao {
    long add(Order order);
    Order findById(long orderId);
    List<Order> findByUser(long userId, OrderSortingCriteria sortingCriteria, SortingOrder sortingOrder, String offset,
                           int limit);
}
