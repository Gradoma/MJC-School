package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class TagDaoImpl implements TagDao {
    private final JdbcTemplate jdbcTemplate;
    private static final String SELECT_ALL = "SELECT Id, Name FROM tag";

    @Autowired
    public TagDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new TagMapper());
    }

    @Override
    public Tag findByName(String name) {
        return null;
    }
}
