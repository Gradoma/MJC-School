package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import static com.epam.esm.dao.column.TagTableConst.*;
import com.epam.esm.dao.mapper.TagMapper;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateException;
import com.epam.esm.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class TagDaoImpl implements TagDao {
    private static final String COUNT_BY_NAME = "SELECT COUNT(*) FROM tag WHERE id=? AND name=?";
    private static final String SELECT_ALL = "SELECT id, Name FROM tag";
    private static final String SELECT_BY_ID = "SELECT id, name FROM tag WHERE id=?";
    private static final String SELECT_BY_NAME = "SELECT id, name FROM tag WHERE name=?";
    private static final String DELETE_BY_ID = "DELETE FROM tag WHERE id=?";
    private static final String SELECT_BY_CERTIFICATE_ID = "SELECT tag.id, tag.Name FROM tag " +
            "JOIN tag_certificate ON tag_certificate.tag_id = tag.id " +
            "JOIN giftcertificate ON giftcertificate.id = tag_certificate.certificate_id " +
            "WHERE giftcertificate.id=?";
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final TagMapper tagMapper;

    public TagDaoImpl(JdbcTemplate jdbcTemplate, TagMapper tagMapper){
        this.tagMapper = tagMapper;
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName(TABLE_TAG)
                .usingGeneratedKeyColumns(ID);
    }

    @Override
    public long add(Tag tag) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(NAME, tag.getName());
        Number generatedId;
        try {
            generatedId = simpleJdbcInsert.executeAndReturnKey(parameters);
        } catch (DuplicateKeyException e){
            throw new DuplicateException("Tag:name=" + tag.getName());
        }
        return generatedId.longValue();
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query(SELECT_ALL, tagMapper);
    }

    @Override
    public Tag findByName(String name){
        try {
            Tag tag = jdbcTemplate.queryForObject(SELECT_BY_NAME, tagMapper, name);
            return tag;
        } catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Tag: name=" + name);
        }
    }

    @Override
    public Tag findById(long id) {
        try{
            Tag tag = jdbcTemplate.queryForObject(SELECT_BY_ID, tagMapper, id);
            return tag;
        } catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Tag: id=" + id);
        }
    }

    @Override
    public List<Tag> findByCertificateId(long certificateId) {
        List<Tag> tagList;
        tagList = jdbcTemplate.query(SELECT_BY_CERTIFICATE_ID, tagMapper, certificateId);
        if(tagList.size() == 0){
            throw new ResourceNotFoundException("Tags for Gift Certificate: id=" + certificateId);
        }
        return tagList;
    }

    @Override
    public boolean deleteById(long id) {
        int rows = jdbcTemplate.update(DELETE_BY_ID, id);
        if (rows > 0 ){
            return true;
        } else {
            throw new ResourceNotFoundException("Tag: id=" + id);
        }
    }

    @Override
    public boolean contains(Tag tag) {
        Integer rows = jdbcTemplate.queryForObject(COUNT_BY_NAME, Integer.class, tag.getId(), tag.getName());
        return rows > 0;
    }
}
