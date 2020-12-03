package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.builder.QueryBuilder;
import com.epam.esm.dao.criteria.QueryCriteria;
import com.epam.esm.dao.mapper.GiftCertificateMapper;
import com.epam.esm.dao.util.HibernateUtil;
import com.epam.esm.dto.CertificateCriteria;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ResourceNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

import static com.epam.esm.dao.column.GiftCertificateTableConst.*;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {
    private static final String UPDATE = "UPDATE giftcertificate SET name = ?, description = ?, price = ?, " +
            "last_update_date = ?, duration_days = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM giftcertificate WHERE id = :id";
    private static final String TAG_CERT_INSERT = "INSERT INTO tag_certificate (tag_id, certificate_id) VALUES (?, ?)";
    private static final String TAG_CERT_DELETE_BY_TAG_AND_CERT_ID = "DELETE FROM tag_certificate WHERE " +
            "tag_id = ? AND certificate_id = ?";
    private static final String PERCENTAGE = "%";

    private final JdbcTemplate jdbcTemplate;
    private final SessionFactory sessionFactory;

    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate, SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public long add(GiftCertificate certificate) {
        Session session = sessionFactory.getCurrentSession();
        session.save(certificate);
        return certificate.getId();
    }

    @Override
    public GiftCertificate findById(long id) {      //TODO(incorrect timezone - need UTC as in db)
        String hql = "FROM GiftCertificate WHERE id=:id";
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(hql);
        query.setParameter("id", id);
        GiftCertificate certificate = (GiftCertificate) query.uniqueResult();
        if (certificate == null){
            throw new ResourceNotFoundException("Gift certificate: id=" + id);
        }
        return certificate;
    }

    @Override
    public List<GiftCertificate> findByCriteria(QueryCriteria queryCriteria) {
        CertificateCriteria criteria = (CertificateCriteria) queryCriteria;
        String sqlQuery = QueryBuilder.makeQuerySelectCertificateWithConditions(criteria);
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
        Session session = sessionFactory.openSession();
        NativeQuery<GiftCertificate> query = session.createNativeQuery(sqlQuery, GiftCertificate.class);
        query.setFirstResult(criteria.getFirstResult());
        query.setMaxResults(criteria.getResultLimit());
        if(queryParams.size() > 0){
            for(int i = 0; i < queryParams.size(); i++){
                query.setParameter(i + 1, queryParams.get(i));
            }
        }
        List<GiftCertificate> resultList = query.list();
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
        Session session = sessionFactory.getCurrentSession();
        session.update(certificate);
        return true;
    }

    @Override
    public boolean patch(GiftCertificate certificate) {
        String sqlQuery = QueryBuilder.makeCertificatePatchQuery(certificate);
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(sqlQuery);
        int rows = query.executeUpdate();
        if(rows == 1){
            return true;
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public boolean delete(long id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(DELETE_BY_ID);
        query.setParameter("id", id);
        int rows = query.executeUpdate();
        if(rows == 1){
            return true;
        } else {
            throw new RuntimeException();
        }
    }

    private void addTagToCertId(long tagId, long certificateId){
        jdbcTemplate.update(TAG_CERT_INSERT, tagId, certificateId);
    }

    private boolean deleteTagToCert(long certificateId, long tagId) {
        int rows = jdbcTemplate.update(TAG_CERT_DELETE_BY_TAG_AND_CERT_ID, tagId, certificateId);
        return rows == 1;
    }

    private LocalDateTime convertToUtcLocalDateTime(ZonedDateTime zonedDateTime){
        return zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }
}
