package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.builder.QueryBuilder;
import com.epam.esm.dao.criteria.QueryCriteria;
import com.epam.esm.entity.User;
import com.epam.esm.exception.DuplicateException;
import com.epam.esm.exception.ResourceNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    private final SessionFactory sessionFactory;

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public long add(User user) {
        Session session = sessionFactory.getCurrentSession();
        try{
            session.save(user);
        } catch (ConstraintViolationException e){
            throw new DuplicateException("User:name=" + user.getName());
        }
        return user.getId();
    }

    @Override
    public User findById(long id) {
        String hql = "FROM User WHERE id=:id";
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(hql);
        query.setParameter("id", id);
        User user = (User) query.uniqueResult();
        if (user == null){
            throw new ResourceNotFoundException("User: id=" + id);
        }
        return user;
    }

    @Override
    public List<User> findAll(QueryCriteria criteria) {
        Session session = sessionFactory.openSession();
        StringBuilder builder = new StringBuilder("FROM User u");
        builder.append(QueryBuilder.addSorting(criteria.getSortingCriteria(), criteria.getSortingOrder().toString()));
        Query<User> query = session.createQuery(builder.toString());
        query.setMaxResults(criteria.getResultLimit());
        query.setFirstResult(criteria.getFirstResult());
        List<User> resultList = query.list();
        if(resultList.size() == 0){
            throw new ResourceNotFoundException();
        }
        return resultList;
    }
}
