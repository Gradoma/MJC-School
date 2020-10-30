package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import static com.epam.esm.dao.column.TagTableConst.*;
import com.epam.esm.dao.mapper.TagMapper;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TagDaoImpl implements TagDao {
    private static final TagMapper tagMapper = new TagMapper();
    private static final String SELECT_ALL = "SELECT Id, Name FROM tag";
    private static final String SELECT_BY_ID = "SELECT Id, Name FROM tag WHERE Id=?";
    private static final String SELECT_BY_NAME = "SELECT Id, Name FROM tag WHERE Name=?";
    private static final String DELETE_BY_ID = "DELETE FROM tag WHERE Id=?";
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public TagDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName(TABLE_TAG)
                .usingGeneratedKeyColumns(ID);
    }

    @Override
    public long add(Tag tag) {
        // check in service
//        Optional<Tag> optionalTag = findByName(tag.getName());
//        if(optionalTag.isPresent()){
//            return optionalTag.get().getId();
//        }
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
    public Optional<Tag> findByName(String name) throws DaoException{
        return selectSingleRow(SELECT_BY_NAME, name);
    }

    @Override
    public Optional<Tag> findById(long id) throws DaoException{
        return selectSingleRow(SELECT_BY_ID, id);
    }

    @Override
    public boolean deleteById(long id) {
        int rows = jdbcTemplate.update(DELETE_BY_ID, id);
        return rows > 0;
    }

    private Optional<Tag> selectSingleRow(String sql, Object param) throws DaoException{
        List<Tag> resultList = jdbcTemplate.query(sql, tagMapper, param);
        if(resultList.size() == 0){
            return Optional.empty();
        } else if (resultList.size() == 1){
            return Optional.of(resultList.get(0));
        } else {
            throw new DaoException("Incorrect result size: expected 1, actual " + resultList.size());
        }
    }
}
