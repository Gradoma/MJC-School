package com.epam.esm.dao;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;

import java.util.List;

public interface OrderDao {
    long add(Order order);
    Order findById(long orderId);
    List<Order> findByUser(long userId);
}
