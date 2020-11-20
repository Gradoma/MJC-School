package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.mapper.OrderDtoMapper;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final OrderDtoMapper orderDtoMapper;
    private final GiftCertificateService giftCertificateService;

    public OrderServiceImpl(OrderDao orderDao, OrderDtoMapper orderDtoMapper,
                            GiftCertificateService giftCertificateService){
        this.giftCertificateService = giftCertificateService;
        this.orderDao = orderDao;
        this.orderDtoMapper = orderDtoMapper;
    }

    @Override
    public long add(OrderDto orderDto) {
        Order order = orderDtoMapper.toEntity(orderDto);
        GiftCertificateDto certificateDto = giftCertificateService.getById(order.getCertificateId());
        order.setCost(Double.parseDouble(certificateDto.getPrice()));
        if(order.getPurchaseDate() == null){
            order.setPurchaseDate(ZonedDateTime.now());
        }
        return orderDao.add(order);
    }

    @Override
    public List<OrderDto> getByUserId(long userId) {
        List<Order> orderList = orderDao.findByUser(userId);
        return orderDtoMapper.toDto(orderList);
    }

    @Override
    public OrderDto getById(long orderId) {
        return orderDtoMapper.toDto(orderDao.findById(orderId));
    }
}
