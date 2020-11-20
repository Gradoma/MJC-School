package com.epam.esm.dao.mapper;

import static com.epam.esm.dao.column.OrderTableConst.*;
import com.epam.esm.entity.Order;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Component
public class OrderMapper implements RowMapper<Order> {
    @Override
    public Order mapRow(ResultSet resultSet, int i) throws SQLException {
        Order order = new Order();
        order.setId(resultSet.getLong(ID));
        order.setUserId(resultSet.getLong(USER_ID));
        order.setCertificateId(resultSet.getLong(CERTIFICATE_ID));
        order.setCost(resultSet.getDouble(COST));
        LocalDateTime purchaseLocalDateTime = resultSet.getObject(PURCHASE_TIME, LocalDateTime.class);
        order.setPurchaseDate(ZonedDateTime.of(purchaseLocalDateTime, ZoneOffset.UTC));

        return order;
    }
}
