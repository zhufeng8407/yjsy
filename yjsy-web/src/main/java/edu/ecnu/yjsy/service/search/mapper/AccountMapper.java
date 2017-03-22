package edu.ecnu.yjsy.service.search.mapper;

import edu.ecnu.yjsy.constant.QueryParameter;
import edu.ecnu.yjsy.model.constant.Column;
import edu.ecnu.yjsy.model.constant.Table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author xiafan
 */
public class AccountMapper extends CommonRowMapper {
    public AccountMapper(CommonRowMapper parent) {
        super(parent);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Map<String, Object> ret = (Map<String, Object>) super.mapRow(rs,
                rowNum);

        ret.put(QueryParameter.ACCOUNT_ID,
                rs.getString(column(Table.ACCOUNT, Column.ID)));
        ret.put(QueryParameter.ACCOUNT_USERNAME,
                rs.getString(Column.ACCOUNT_USERNAME));

        return ret;
    }

}
