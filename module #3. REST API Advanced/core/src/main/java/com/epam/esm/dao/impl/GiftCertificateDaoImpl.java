package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.builder.QueryBuilder;
import com.epam.esm.dao.mapper.GiftCertificateMapper;
import com.epam.esm.dao.util.HibernateUtil;
import com.epam.esm.dto.CertificateCriteria;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateException;
import com.epam.esm.exception.ResourceNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.esm.dao.column.GiftCertificateTableConst.*;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {
    private static final String SELECT_BY_ID = "SELECT giftcertificate.id, giftcertificate.name, description, price, " +
            "create_date, last_update_date, duration_days FROM giftcertificate " +
            "WHERE giftcertificate.id = :id";
    private static final String UPDATE = "UPDATE giftcertificate SET name = ?, description = ?, price = ?, " +
            "last_update_date = ?, duration_days = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM giftcertificate WHERE id = :id";
    private static final String TAG_CERT_INSERT = "INSERT INTO tag_certificate (tag_id, certificate_id) VALUES (?, ?)";
    private static final String TAG_CERT_DELETE_BY_TAG_AND_CERT_ID = "DELETE FROM tag_certificate WHERE " +
            "tag_id = ? AND certificate_id = ?";
    private static final String PERCENTAGE = "%";

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final GiftCertificateMapper giftMapper;
    private final SessionFactory sessionFactory;

    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate, GiftCertificateMapper giftMapper,
                                  NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                  SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
        this.giftMapper = giftMapper;
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName(TABLE_CERTIFICATE)
                .usingGeneratedKeyColumns(ID);
    }
    @Override
    public long add(GiftCertificate certificate) {      //todo (different type of cascade-why wasn't work with all)
        Session session = sessionFactory.getCurrentSession();
        session.save(certificate);
        return certificate.getId();
    }

    @Override
    public GiftCertificate findById(long id) {      //TODO(incorrect timezone - need UTC as in db)
        String hql = "FROM GiftCertificate WHERE id=:id";
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery(hql);
        query.setParameter("id", id);
        GiftCertificate certificate = (GiftCertificate) query.uniqueResult();
        if (certificate == null){
            throw new ResourceNotFoundException("Gift certificate: id=" + id);
        }
        return certificate;
    }

    @Override
    public List<GiftCertificate> findByCriteria(CertificateCriteria criteria) {
        List<GiftCertificate> resultList = new ArrayList<>();
        String query = QueryBuilder.makeQuery(criteria);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        if(criteria.getTags() != null){
            parameters.addValue("tag_names", criteria.getTags());
        }
        if(criteria.getName() != null){
            parameters.addValue("name", addPercentageWildcard(criteria.getName()));
        }
        if(criteria.getDescription() != null){
            parameters.addValue("description", addPercentageWildcard(criteria.getDescription()));
        }
        resultList = namedParameterJdbcTemplate.query(query,
                parameters, giftMapper);
        if(resultList.size() == 0){
            throw new ResourceNotFoundException("Gift certificate: name=" + criteria.getName() +
                    ", description=" + criteria.getDescription() +
                    ", tags=" + criteria.getTags());
        }
        return resultList;
    }

    @Override
    public List<GiftCertificate> findByCriteriaAndCondition(CertificateCriteria criteria) {
        List<GiftCertificate> resultList = new ArrayList<>();
        String query = QueryBuilder.makeQueryAndTagCondition(criteria);
        List<String> queryParams = new ArrayList<>();
        if(criteria.getName() != null){
            queryParams.add(addPercentageWildcard(criteria.getName()));
        }
        if(criteria.getDescription() != null){
            queryParams.add(addPercentageWildcard(criteria.getDescription()));
        }
        if(criteria.getTags() != null){
            queryParams.addAll(criteria.getTags());
        }
        if(queryParams.size() > 0){
            String[] paramsArray = queryParams.stream().toArray(String[]::new);
            resultList = jdbcTemplate.query(query, giftMapper, paramsArray);
        } else {
            resultList = jdbcTemplate.query(query, giftMapper);
        }
        if(resultList.size() == 0){
            throw new ResourceNotFoundException("Gift certificate: name=" + criteria.getName() +
                    ", description=" + criteria.getDescription() +
                    ", tags=" + criteria.getTags());
        }
        return resultList;
    }

    private String addPercentageWildcard(String param){
        return PERCENTAGE + param.trim() + PERCENTAGE;
    }

    @Override
    public boolean update(GiftCertificate certificate, List<Long> addedTagsId, List<Long> deletedTagsId) {
        String updateHql = "UPDATE GiftCertificate SET name = :name, description = :description, price = :price, " +
                "lastUpdateDate = :lastUpdateDate, duration = :duration WHERE id = :id";
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Query insertTagQuery = session.createSQLQuery(TAG_CERT_INSERT);
        insertTagQuery.setParameter(2, certificate.getId());
        addedTagsId.forEach(tagIdToAdd -> {
            insertTagQuery.setParameter(1, tagIdToAdd);
            insertTagQuery.executeUpdate();
        });
        Query deleteTagQuery = session.createSQLQuery(TAG_CERT_DELETE_BY_TAG_AND_CERT_ID);
        deleteTagQuery.setParameter(2, certificate.getId());
        deletedTagsId.forEach(tagIdToDelete -> {
            deleteTagQuery.setParameter(1, tagIdToDelete);
            deleteTagQuery.executeUpdate();
        });

        Query updateQuery = session.createQuery(updateHql);
        updateQuery.setParameter("id", certificate.getId());
        updateQuery.setParameter("name", certificate.getName());
        updateQuery.setParameter("description", certificate.getDescription());
        updateQuery.setParameter("price", certificate.getPrice());
        updateQuery.setParameter("lastUpdateDate", certificate.getLastUpdateDate());
        updateQuery.setParameter("duration", certificate.getDuration());
        int rows = updateQuery.executeUpdate();
        if (rows > 0 ){
            transaction.commit();
            return true;
        } else {
            transaction.rollback();
            return false;
        }
    }

    @Override
    public boolean patch(GiftCertificate certificate) {
        String sqlQuery = QueryBuilder.makePatchQuery(certificate);
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createSQLQuery(sqlQuery);
        int rows = query.executeUpdate();
        if (rows > 0 ){
            transaction.commit();
            return true;
        } else {
            transaction.rollback();
            throw new ResourceNotFoundException("Gift certificate: id=" + certificate.getId());
        }
    }

    @Override
    public boolean delete(long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createSQLQuery(DELETE_BY_ID);
        query.setParameter("id", id);
        int rows = query.executeUpdate();
        if (rows > 0 ){
            transaction.commit();
            return true;
        } else {
            transaction.rollback();
            throw new ResourceNotFoundException("Gift certificate: id=" + id);
        }
    }

    private void addTagToCertId(long tagId, long certificateId){
        // how to use hibernate - no entity for 3rd table
        jdbcTemplate.update(TAG_CERT_INSERT, tagId, certificateId);
    }

    private boolean deleteTagToCert(long certificateId, long tagId) {
        // how to use hibernate - no entity for 3rd table
        int rows = jdbcTemplate.update(TAG_CERT_DELETE_BY_TAG_AND_CERT_ID, tagId, certificateId);
        return rows == 1;
    }

    private LocalDateTime convertToUtcLocalDateTime(ZonedDateTime zonedDateTime){
        return zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }
}
