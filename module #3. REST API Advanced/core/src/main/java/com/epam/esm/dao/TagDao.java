package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.List;

public interface TagDao {
    long add(Tag tag);
    List<Tag> findAll();
    Tag findByName(String name);
    Tag findById(long id);
    Tag findMostPopular();
    List<Tag> findByCertificateId(long certificateId);
    boolean deleteById(long id);
    boolean contains(Tag tag);
}
