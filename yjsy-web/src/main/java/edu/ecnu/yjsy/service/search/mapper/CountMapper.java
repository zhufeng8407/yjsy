package edu.ecnu.yjsy.service.search.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CountMapper extends CommonRowMapper {

    public CountMapper(CommonRowMapper parent) {
        super(parent);
    }

    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        //没有必要调用chain前面的processor
        return rs.getLong("count(*)");
    }

}
