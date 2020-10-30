package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface TagDao {
    long add(Tag tag);
    List<Tag> findAll();
    Optional<Tag> findByName(String name) throws DaoException;
    Optional<Tag> findById(long id) throws DaoException;
    boolean deleteById(long id);
}
