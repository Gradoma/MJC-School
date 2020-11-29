package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.builder.QueryBuilder;
import com.epam.esm.dao.mapper.OrderMapper;
import com.epam.esm.entity.Order;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.sorting.OrderSortingCriteria;
import com.epam.esm.service.sorting.SortingOrder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.esm.dao.column.OrdersTableConst.*;

@Repository
public class OrderDaoImpl implements OrderDao {
    private static final String SELECT_BY_ID = "SELECT id, user_id, certificate_id, cost, purchase_time " +
            "FROM orders WHERE id=?";
    private static final String SELECT_BY_USER_ID = "SELECT id, user_id, certificate_id, cost, purchase_time " +
            "FROM orders WHERE user_id=?";

    private final JdbcTemplate jdbcTemplate;
    public final OrderMapper orderMapper;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDaoImpl(JdbcTemplate jdbcTemplate, OrderMapper orderMapper){
        this.jdbcTemplate = jdbcTemplate;
        this.orderMapper = orderMapper;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName(TABLE_ORDERS)
                .usingGeneratedKeyColumns(ID);
    }

    @Override
    public long add(Order order) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(USER_ID, order.getUserId());
        parameters.put(CERTIFICATE_ID, order.getCertificateId());
        parameters.put(COST, order.getCost());
        parameters.put(PURCHASE_TIME, convertToUtcLocalDateTime(order.getPurchaseDate()));
        long orderId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();

        return orderId;
    }

    @Override
    public Order findById(long orderId) {
        try{
            return jdbcTemplate.queryForObject(SELECT_BY_ID, orderMapper, orderId);
        } catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("SortingOrder: id=" + orderId);
        }
    }

    @Override
    public List<Order> findByUser(long userId, OrderSortingCriteria sortingCriteria, SortingOrder sortingOrder,
                                  String offset, int limit) {
        StringBuilder builder = new StringBuilder(SELECT_BY_USER_ID);
        if(offset != null){
            builder.append(QueryBuilder.addOffset(sortingCriteria.getColumn(), offset, sortingOrder, false));
        }
        builder.append(QueryBuilder.addSorting(sortingCriteria.getColumn(), sortingOrder.toString()));
        builder.append(QueryBuilder.addLimit(limit));
        List<Order> orderList;
        orderList = jdbcTemplate.query(builder.toString(), orderMapper, userId);
        if(orderList.size() == 0){
            throw new ResourceNotFoundException("orders for User: userId=" + userId);
        }
        return orderList;
    }

    private LocalDateTime convertToUtcLocalDateTime(ZonedDateTime zonedDateTime){
        return zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }
}
