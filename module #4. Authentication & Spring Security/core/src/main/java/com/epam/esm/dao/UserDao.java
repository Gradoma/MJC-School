package com.epam.esm.dao;

import com.epam.esm.dao.criteria.QueryCriteria;
import com.epam.esm.entity.User;

import java.util.List;

public interface UserDao {
    long add(User user);
    User findById(long id);
    List<User> findAll(QueryCriteria criteria);
}
