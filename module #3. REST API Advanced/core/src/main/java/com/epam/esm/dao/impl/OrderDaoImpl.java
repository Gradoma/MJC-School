package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.builder.QueryBuilder;
import com.epam.esm.dao.criteria.QueryCriteria;
import com.epam.esm.dao.mapper.OrderMapper;
import com.epam.esm.dao.util.HibernateUtil;
import com.epam.esm.entity.Order;
import com.epam.esm.exception.ResourceNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
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
    private final SessionFactory sessionFactory;

    public OrderDaoImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public long add(Order order) {
        Session session = sessionFactory.openSession();
        session.save(order);
        return order.getId();
    }

    @Override
    public Order findById(long orderId) {
        String hql = "FROM Order WHERE id=:id";
        Session session = sessionFactory.openSession();
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
        Session session = sessionFactory.openSession();
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
