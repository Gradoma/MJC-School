package com.epam.esm.dao.mapper;

import com.epam.esm.dao.column.TagConst;
import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TagMapper implements RowMapper<Tag> {

    @Override
    public Tag mapRow(ResultSet resultSet, int i) throws SQLException {
        Tag tag = new Tag();
        tag.setId(resultSet.getLong(TagConst.ID));
        tag.setName(resultSet.getString(TagConst.NAME));
        return tag;
    }


}
