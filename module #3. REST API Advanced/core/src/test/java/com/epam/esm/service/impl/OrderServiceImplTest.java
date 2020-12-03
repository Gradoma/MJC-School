package com.epam.esm.service.impl;

import com.epam.esm.config.TestApp;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.criteria.QueryCriteria;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.mapper.OrderDtoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApp.class)
class OrderServiceImplTest {
    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderDao orderDao;
    @MockBean
    private GiftCertificateService giftCertificateService;

    @Test
    void save() {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setUserId(15L);
        orderDto.setCertificateId(5L);

        GiftCertificateDto certificateDto = new GiftCertificateDto();
        certificateDto.setPrice(55.65);

        Mockito.doReturn(certificateDto).when(giftCertificateService).getById(5);

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);

        orderService.save(orderDto);
        // field date not null
        Mockito.verify(orderDao, Mockito.times(1))
                .add(captor.capture());
        Order order = captor.getValue();
        assertNotNull(order.getPurchaseDate());
        Mockito.verify(giftCertificateService, Mockito.times(1))
                .getById(ArgumentMatchers.eq(orderDto.getCertificateId()));
    }

    @Test
    void getByUserId() {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setUserId(15L);
        orderDto.setCertificateId(5L);

        ArgumentCaptor<QueryCriteria> captor = ArgumentCaptor.forClass(QueryCriteria.class);

        orderService.getByUserId(15, 2);
        //check dao was called 1 time
        Mockito.verify(orderDao, Mockito.times(1))
                .findByUser(ArgumentMatchers.eq(15L), captor.capture());
        // check query params, tim
        QueryCriteria criteria = captor.getValue();
        assertNotNull(criteria.getSortingCriteria());
        assertNotNull(criteria.getSortingOrder());
        assertEquals(5, criteria.getFirstResult());
        assertEquals(5, criteria.getResultLimit());
    }

    @Test
    void getById() {
        long orderId = 5;

        Order order = new Order();
        order.setPurchaseDate(ZonedDateTime.now());
        Mockito.doReturn(order).when(orderDao).findById(orderId);

        orderService.getById(orderId);

        Mockito.verify(orderDao, Mockito.times(1)).findById(ArgumentMatchers.eq(orderId));
    }
}