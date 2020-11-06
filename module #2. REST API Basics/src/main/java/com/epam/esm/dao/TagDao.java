package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateException;

import java.util.List;
import java.util.Optional;

public interface TagDao {
    long add(Tag tag) throws DuplicateException;
    List<Tag> findAll();
    Tag findByName(String name);
    Tag findById(long id);
    boolean deleteById(long id);
}
