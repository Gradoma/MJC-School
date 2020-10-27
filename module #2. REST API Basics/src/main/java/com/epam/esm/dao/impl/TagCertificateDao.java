package com.epam.esm.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.epam.esm.dao.column.TagCertificateConst.*;

public class TagCertificateDao {       //TODO (rename)
    private final JdbcTemplate jdbcTemplate;
    private static final String INSERT = "INSERT INTO tag_certificate (TagId, CertificateId) VALUES (?, ?)";
    private static final String SELECT_BY_TAG_ID = "SELECT CertificateId FROM tag_certificate WHERE TagId=?";
    private static final String SELECT_BY_CERTIFICATE_ID = "SELECT TagId FROM tag_certificate WHERE CertificateId=?";
    private static final String UPDATE = "UPDATE tag_certificate SET TagId = ? WHERE TagId = ? AND " +
            "CertificateId = ?";
    private static final String DELETE = "DELETE FROM tag_certificate WHERE TagId = ? AND CertificateId = ?";

    public TagCertificateDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    void add(long tagId, long certificateId){
        jdbcTemplate.update(INSERT, tagId, certificateId);
    }

    List<Long> findByTagId(long tagId) {
        return jdbcTemplate.query(SELECT_BY_TAG_ID, new RowMapper<Long>() {
            @Override
            public Long mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getLong(CERTIFICATE_ID);
            }
        });
    }

    List<Long> findByCertificateId(long certificateId) {
        return jdbcTemplate.query(SELECT_BY_CERTIFICATE_ID, new RowMapper<Long>() {
            @Override
            public Long mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getLong(TAG_ID);
            }
        });
    }

    boolean updateByCertificateId(long tagId, long certificateId, long newTagId) {
        int rows = jdbcTemplate.update(UPDATE, newTagId, tagId, certificateId);
        return rows == 1;
    }

    boolean deleteTagByCertificateId(long tagId, long certificateId) {
        int rows = jdbcTemplate.update(DELETE, tagId, certificateId);
        return rows == 1;
    }
}

