package edu.ecnu.yjsy.service.search.mapper;

import edu.ecnu.yjsy.constant.QueryParameter;
import edu.ecnu.yjsy.model.constant.Column;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author xiafan
 */
public class AccountPrivilegeMapper extends CommonRowMapper {

    public AccountPrivilegeMapper(CommonRowMapper parent) {
        super(parent);
        boolFields.put(QueryParameter.IS_UNIVERSITY, Column.ACCOUNT_PRIVILEGE_UNIV);
        stringFields.put(QueryParameter.SCHOOL_CODE, Column.META_SCHOOL_CODE);
        stringFields.put(QueryParameter.DEPARTMENT_CODE, Column.META_DEPARTMENT_CODE);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Map<String, Object> ret = (Map<String, Object>) super
                .mapRow(rs, rowNum);

        if (rs.getBoolean(Column.ACCOUNT_PRIVILEGE_UNIV)) {
            ret.put(QueryParameter.IS_UNIVERSITY, "是");
        } else if (rs.getString(Column.ACCOUNT_PRIVILEGE_SCHOOL) != null) {
            ret.put(QueryParameter.SCHOOL_CODE,
                    rs.getString(Column.ACCOUNT_PRIVILEGE_SCHOOL));
        } else if (rs.getString(Column.ACCOUNT_PRIVILEGE_DEPART) != null) {
            ret.put(QueryParameter.DEPARTMENT_CODE,
                    rs.getString(Column.ACCOUNT_PRIVILEGE_DEPART));
        } else if (rs.getString(Column.ACCOUNT_PRIVILEGE_SUPERVISOR) != null) {
            ret.put(QueryParameter.SUPERVISOR_ID,
                    rs.getBoolean(Column.ACCOUNT_PRIVILEGE_SUPERVISOR));
        } else if (rs.getString(Column.ACCOUNT_PRIVILEGE_STUDENT) != null) {
            ret.put(QueryParameter.STUDENT_ID,
                    rs.getString(Column.ACCOUNT_PRIVILEGE_STUDENT));
        }

        return ret;
    }

}
