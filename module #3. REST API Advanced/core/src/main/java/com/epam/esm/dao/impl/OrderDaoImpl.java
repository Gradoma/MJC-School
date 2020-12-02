package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.builder.QueryBuilder;
import com.epam.esm.dao.criteria.QueryCriteria;
import com.epam.esm.dao.mapper.OrderMapper;
import com.epam.esm.dao.util.HibernateUtil;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.sorting.OrderSortingCriteria;
import com.epam.esm.service.sorting.SortingOrder;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.save(order);
            return order.getId();
        }
    }

    @Override
    public Order findById(long orderId) {
        String hql = "FROM Order WHERE id=:id";
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery(hql);
        query.setParameter("id", orderId);
        Order resultOrder = (Order) query.uniqueResult();
        if (resultOrder == null){
            throw new ResourceNotFoundException(" Order: id=" + orderId);
        }
        return resultOrder;
    }

    @Override
    public List<Order> findByUser(long userId, QueryCriteria criteria) {
        Session session = HibernateUtil.getSessionFactory().openSession();//todo (session from Util or add bean SessionFactories
        StringBuilder builder = new StringBuilder("FROM Order o WHERE o.userId = :userId");
        builder.append(QueryBuilder.addSorting(criteria.getSortingCriteria(), criteria.getSortingOrder().toString()));
        Query<Order> query = session.createQuery(builder.toString());
        query.setParameter("userId", userId);
        query.setMaxResults(criteria.getResultLimit());
        query.setFirstResult(criteria.getFirstResult());
        List<Order> resultList = query.list();
        if(resultList.size() == 0){
            throw new ResourceNotFoundException(" userId=" + userId);
        }
        return resultList;
    }

    private LocalDateTime convertToUtcLocalDateTime(ZonedDateTime zonedDateTime){
        return zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }
}
