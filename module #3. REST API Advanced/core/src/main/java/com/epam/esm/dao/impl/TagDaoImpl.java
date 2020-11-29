package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.builder.QueryBuilder;
import com.epam.esm.dao.mapper.TagMapper;
import com.epam.esm.dao.util.HibernateUtil;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.sorting.SortingOrder;
import com.epam.esm.service.sorting.TagSortingCriteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.esm.dao.column.TagTableConst.*;

@Repository
public class TagDaoImpl implements TagDao {
    private static final String COUNT_BY_NAME = "SELECT COUNT(*) FROM tag WHERE id=? AND name=?";
    private static final String SELECT_ALL = "SELECT id, name FROM tag";
    private static final String SELECT_BY_ID = "SELECT id, name FROM tag WHERE id=?";
    private static final String SELECT_BY_NAME = "SELECT id, name FROM tag WHERE name=?";
    private static final String DELETE_BY_ID = "DELETE FROM tag WHERE id=?";
    private static final String SELECT_BY_CERTIFICATE_ID = "SELECT tag.id, tag.Name FROM tag " +
            "JOIN tag_certificate ON tag_certificate.tag_id = tag.id " +
            "JOIN giftcertificate ON giftcertificate.id = tag_certificate.certificate_id " +
            "WHERE giftcertificate.id=?";
    private static final String SELECT_MOST_POPULAR_TAG = "SELECT tag.id, tag.name " +
            "FROM tag " +
            "JOIN tag_certificate ON tag_certificate.tag_id = tag.id " +
            "JOIN giftcertificate ON giftcertificate.id = tag_certificate.certificate_id " +
            "WHERE giftcertificate.id IN " +
            "(SELECT certificate_id FROM orders " +
            "WHERE user_id= " +
            "    (SELECT user_id FROM orders WHERE cost = (SELECT MAX(cost) FROM orders)) " +
            "    ) " +
            "GROUP BY tag.name " +
            "ORDER BY COUNT(tag.name) DESC " +
            "LIMIT 1";
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
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try{
            session.save(tag);
            transaction.commit();
        } catch (ConstraintViolationException e){
            throw new DuplicateException("Tag:name=" + tag.getName());
        } finally {
            session.close();
        }
        return tag.getId();
    }

    @Override
    public List<Tag> findAll(TagSortingCriteria sortingCriteria, SortingOrder sortingOrder, String offset, int limit) {
        StringBuilder builder = new StringBuilder(SELECT_ALL);
        if(offset != null){
            builder.append(QueryBuilder.addOffset(sortingCriteria.getColumn(), offset, sortingOrder, true));
        }
        builder.append(QueryBuilder.addSorting(sortingCriteria.getColumn(), sortingOrder.toString()));
        builder.append(QueryBuilder.addLimit(limit));
        return jdbcTemplate.query(builder.toString(), tagMapper);
    }

    @Override
    public Tag findByName(String name){
        try {
            return jdbcTemplate.queryForObject(SELECT_BY_NAME, tagMapper, name);
        } catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Tag: name=" + name);
        }
    }

    @Override
    public Tag findById(long id) {
        try{
            return jdbcTemplate.queryForObject(SELECT_BY_ID, tagMapper, id);
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
    public Tag findMostPopular() {
        return jdbcTemplate.queryForObject(SELECT_MOST_POPULAR_TAG, tagMapper);
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
