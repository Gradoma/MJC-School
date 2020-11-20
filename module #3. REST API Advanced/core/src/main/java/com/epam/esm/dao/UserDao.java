package com.epam.esm.dao;

import com.epam.esm.entity.User;

public interface UserDao {
    User findById(long id);
}
