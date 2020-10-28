package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDao {
    long add(Tag tag);
    List<Tag> findAll();
    Optional<Tag> findByName(String name);
    Optional<Tag> findById(long id);
    boolean update(Tag tag);
    boolean deleteById(long id);
}
