package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import static com.epam.esm.dao.column.TagConst.*;
import com.epam.esm.dao.mapper.TagMapper;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagDaoImpl implements TagDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private static final TagMapper tagMapper = new TagMapper();
    private static final String SELECT_ALL = "SELECT Id, Name FROM tag";
    private static final String SELECT_BY_ID = "SELECT Id, Name FROM tag WHERE Id=?";
    private static final String SELECT_BY_NAME = "SELECT Id, Name FROM tag WHERE Name=?";
    private static final String DELETE_BY_ID = "DELETE FROM tag WHERE Id=?";
    private static final String UPDATE = "UPDATE tag SET Name = ? WHERE Id = ?";

    @Autowired
    public TagDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName(TABLE_TAG)
                .usingGeneratedKeyColumns(ID);
    }

    @Override
    public long add(Tag tag) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(NAME, tag.getName());
        Number generatedId = simpleJdbcInsert.executeAndReturnKey(parameters);
        return generatedId.longValue();
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query(SELECT_ALL, tagMapper);
    }

    @Override
    public Tag findByName(String name) {
        return jdbcTemplate.queryForObject(SELECT_BY_NAME, Tag.class, name);
    }

    @Override
    public Tag findById(long id) {
        return jdbcTemplate.queryForObject(SELECT_BY_ID, Tag.class, id);
    }

    @Override
    public boolean update(Tag tag) {
        int rows = jdbcTemplate.update(UPDATE, tag.getName(), tag.getId());
        return rows > 0;
    }

    @Override
    public boolean deleteById(long id) {
        int rows = jdbcTemplate.update(DELETE_BY_ID, id);
        return rows > 0;
    }
}
