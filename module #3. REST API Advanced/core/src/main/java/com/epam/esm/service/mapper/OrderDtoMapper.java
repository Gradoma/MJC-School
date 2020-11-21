package com.epam.esm.service.mapper;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderDtoMapper {
    public Order toEntity(OrderDto orderDto){
        Order order = new Order();
        order.setUserId(Long.parseLong(orderDto.getUserId()));
        order.setCertificateId(Long.parseLong(orderDto.getCertificateId()));
        if(orderDto.getPurchase_date() != null){
            order.setPurchaseDate(ZonedDateTime.parse(orderDto.getPurchase_date()));
        }
        return order;
    }

    public OrderDto toDto(Order order){
        OrderDto orderDto = new OrderDto();
        orderDto.setId(Long.toString(order.getId()));
        orderDto.setUserId(Long.toString(order.getUserId()));
        orderDto.setCertificateId(Long.toString(order.getCertificateId()));
        orderDto.setCost(Double.toString(order.getCost()));
        orderDto.setPurchase_date(order.getPurchaseDate().toString());

        return orderDto;
    }

    public List<OrderDto> toDto(List<Order> orderList){
        List<OrderDto> resultList = new ArrayList<>();
        orderList.forEach(order -> resultList.add(toDto(order)));
        return resultList;
    }
}
