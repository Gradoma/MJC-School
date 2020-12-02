package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;

import java.util.List;

public interface OrderService {
    long save(OrderDto orderDto);
    List<OrderDto> getByUserId(long userId, int page);
    OrderDto getById(long orderId);
}
