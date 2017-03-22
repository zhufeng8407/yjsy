package edu.ecnu.yjsy.service.search.mapper;

import edu.ecnu.yjsy.constant.QueryParameter;
import edu.ecnu.yjsy.model.constant.Column;
import edu.ecnu.yjsy.model.constant.Table;

/**
 * @author xiafan
 */
public class StaffMapper extends CommonRowMapper {

    public StaffMapper(CommonRowMapper parent) {
        super(parent);
        stringFields.put(QueryParameter.STAFF_NAME, column(Table.STAFF, Column.STAFF_NAME));
        stringFields.put(QueryParameter.STAFF_NO, column(Table.STAFF, Column.STAFF_SNO));
        stringFields.put(QueryParameter.STAFF_ID, column(Table.STAFF, Column.ID));
    }

}
