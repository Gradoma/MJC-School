package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;

import java.util.List;

public interface OrderService {
    long save(OrderDto orderDto);
    List<OrderDto> getByUserId(long userId, int page);
    OrderDto getById(long orderId);
}
