package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.builder.QueryBuilder;
import com.epam.esm.dao.criteria.QueryCriteria;
import com.epam.esm.dao.mapper.TagMapper;
import com.epam.esm.dao.util.HibernateUtil;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.sorting.SortingOrder;
import com.epam.esm.service.sorting.TagSortingCriteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.hibernate.type.IntegerType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private final SessionFactory sessionFactory;

    private final TagMapper tagMapper;

    public TagDaoImpl(JdbcTemplate jdbcTemplate, TagMapper tagMapper, SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
        this.tagMapper = tagMapper;
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName(TABLE_TAG)
                .usingGeneratedKeyColumns(ID);
    }

    @Override
    public long add(Tag tag) {
        Session session = sessionFactory.getCurrentSession();
        try{
            session.save(tag);
        } catch (ConstraintViolationException e){
            throw new DuplicateException("Tag:name=" + tag.getName());
        }
        return tag.getId();
    }

    @Override
    public List<Tag> findAll(QueryCriteria criteria) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        StringBuilder builder = new StringBuilder("FROM Tag t");
        builder.append(QueryBuilder.addSorting(criteria.getSortingCriteria(), criteria.getSortingOrder().toString()));
        Query<Tag> query = session.createQuery(builder.toString());
        query.setMaxResults(criteria.getResultLimit());
        query.setFirstResult(criteria.getFirstResult());
        List<Tag> resultList = query.list();
        if(resultList.size() > 0){
            return resultList;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @Override
    public Tag findByName(String name){
        String hql = "FROM Tag WHERE name=:name";
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery(hql);
        query.setParameter("name", name);
        Tag resultTag = (Tag) query.uniqueResult();
        if (resultTag == null){
            throw new ResourceNotFoundException("Tag: name=" + name);
        }
        return resultTag;
    }

    @Override
    public Tag findById(long id) {
        String hql = "FROM Tag WHERE id=:id";
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery(hql);
        query.setParameter("id", id);
        Tag resultTag = (Tag) query.uniqueResult();
        if (resultTag == null){
            throw new ResourceNotFoundException("Tag: id=" + id);
        }
        return resultTag;
    }

    @Override
    public List<Tag> findByCertificateId(long certificateId) {          // TODO TEST!
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Tag> tagList = session.getNamedNativeQuery("findByCertificateId")
                .setParameter("id", certificateId)
                .list();
        if(tagList.size() == 0){
            throw new ResourceNotFoundException("Tags for Gift Certificate: id=" + certificateId);
        }
        return tagList;
    }

    @Override
    public Tag findMostPopular() {          // todo
        return jdbcTemplate.queryForObject(SELECT_MOST_POPULAR_TAG, tagMapper);
    }

    @Transactional
    @Override
    public boolean deleteById(long id) {
        String hql = "DELETE Tag WHERE id = :id";
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery(hql);
        query.setParameter("id", id);
        int rows = query.executeUpdate();
        if (rows > 0 ){
            transaction.commit();
            return true;
        } else {
            transaction.rollback();
            throw new ResourceNotFoundException("Tag: id=" + id);
        }
    }

    @Override
    public boolean contains(Tag tag) {
        String sql = "SELECT COUNT(*) AS c FROM tag WHERE id = :id AND name = :name";
        Session session = sessionFactory.getCurrentSession();
        Integer rows = (Integer) session.createSQLQuery(sql)
                .setParameter("id", tag.getId())
                .setParameter("name", tag.getName())
                .addScalar("c", IntegerType.INSTANCE)
                .uniqueResult();
        return rows > 0;
    }
}
