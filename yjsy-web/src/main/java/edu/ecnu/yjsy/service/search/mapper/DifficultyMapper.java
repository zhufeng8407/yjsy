package edu.ecnu.yjsy.service.search.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import edu.ecnu.yjsy.model.constant.Column;

public class DifficultyMapper extends CommonRowMapper {
    public static final String DIFF_ID = "diffId";

    public DifficultyMapper(CommonRowMapper parent) {
        super(parent);
        stringFields.put(DIFF_ID, DIFF_ID);
        stringFields.put("year", Column.YEAR);
        boolFields.put("level", Column.LEVEL);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Map<String, Object> ret = (Map<String, Object>) super.mapRow(rs,
                rowNum);
        if (ret.get("level") == null) {
            ret.put("level", "否");
        }

        return ret;
    }

}
