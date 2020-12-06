package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.builder.QueryBuilder;
import com.epam.esm.dao.criteria.QueryCriteria;
import com.epam.esm.dao.mapper.TagMapper;
import com.epam.esm.dao.util.HibernateUtil;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateException;
import com.epam.esm.exception.ResourceNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.NativeQuery;
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
    private static final String SELECT_MOST_POPULAR_TAG = "select id, name from tag where tag.id = " +
            "(select tag_certificate.tag_id from tag_certificate where tag_certificate.certificate_id IN " +
            "(SELECT certificate_id FROM orders " +
            "WHERE user_id= " +
            "(SELECT user_id FROM orders WHERE cost = " +
            "(SELECT MAX(cost) FROM orders))) " +
            "GROUP BY tag_id " +
            "ORDER BY COUNT(tag_id) DESC " +
            "LIMIT 1);";
    private final SessionFactory sessionFactory;

    public TagDaoImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
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
        Session session = sessionFactory.openSession();
        StringBuilder builder = new StringBuilder("FROM Tag t");
        builder.append(QueryBuilder.addSorting(criteria.getSortingCriteria(), criteria.getSortingOrder().toString()));
        Query<Tag> query = session.createQuery(builder.toString());
        query.setMaxResults(criteria.getResultLimit());
        query.setFirstResult(criteria.getFirstResult());
        List<Tag> resultList = query.list();
        if(resultList.size() == 0){
            throw new ResourceNotFoundException();
        }
        return resultList;
    }

    @Override
    public Tag findByName(String name){
        String hql = "FROM Tag WHERE name=:name";
        Session session = sessionFactory.openSession();
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
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(hql);
        query.setParameter("id", id);
        Tag resultTag = (Tag) query.uniqueResult();
        if (resultTag == null){
            throw new ResourceNotFoundException("Tag: id=" + id);
        }
        return resultTag;
    }

    @Override
    public List<Tag> findByCertificateId(long certificateId) {
        Session session = sessionFactory.openSession();
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
        Session session = sessionFactory.openSession();
        NativeQuery<Tag> query = session.createNativeQuery(SELECT_MOST_POPULAR_TAG, Tag.class);
        Tag tag = query.uniqueResult();
        if (tag == null){
            throw new ResourceNotFoundException("Tag not found");
        }
        return tag;
    }

    @Override
    public boolean deleteById(long id) {
        String hql = "DELETE Tag WHERE id = :id";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        query.setParameter("id", id);
        int rows = query.executeUpdate();
        if(rows == 1){
            return true;
        } else {
            throw new RuntimeException();
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
