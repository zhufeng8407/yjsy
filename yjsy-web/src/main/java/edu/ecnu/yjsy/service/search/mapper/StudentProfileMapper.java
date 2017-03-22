package edu.ecnu.yjsy.service.search.mapper;

import edu.ecnu.yjsy.constant.QueryParameter;
import edu.ecnu.yjsy.model.constant.Column;
import edu.ecnu.yjsy.model.constant.Table;
import edu.ecnu.yjsy.model.student.PregraduationStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * 从返回结果中抽取学生的信息简介
 *
 * @author xiafan
 */
public class StudentProfileMapper extends CommonRowMapper {

    public StudentProfileMapper(CommonRowMapper parent) {
        super(parent);
        stringFields.put(QueryParameter.ID, column(Table.STUDENT, Column.ID));
        stringFields.put(QueryParameter.STUDENT_NAME, Column.NAME);
        stringFields.put(QueryParameter.STUDENT_NO, Column.SNO);
        stringFields.put(QueryParameter.STUDENT_PHOTO, Column.PHOTO);
        stringFields.put(QueryParameter.GRADE, Column.GRADE);
        stringFields.put(QueryParameter.DEGREE_TYPE, Column.META_DEGREE_TYPE);

        boolFields.put(QueryParameter.IS_NEW, Column.IS_NEW);

        dateFields.put(QueryParameter.ADMISSION_DATE, Column.IS_CHECKIN);
        stringFields.put(QueryParameter.EDUCATION_MODE, column(Table.EDUCATION_MODE, Column.META_NAME));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Map<String, Object> ret = (Map<String, Object>) super.mapRow(rs,
                rowNum);
        try {
            int statusIdx = rs
                    .getInt(column(Table.STUDENT, Column.PREGRADUATION_STATUS));

            ret.put("pregraduationStatus",
                    PregraduationStatus.values()[statusIdx].toString());
        } catch (SQLException ex) {}

        return ret;
    }

}
