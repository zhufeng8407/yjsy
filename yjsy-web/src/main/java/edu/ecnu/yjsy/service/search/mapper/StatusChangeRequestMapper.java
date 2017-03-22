package edu.ecnu.yjsy.service.search.mapper;

import edu.ecnu.yjsy.model.constant.Column;
import edu.ecnu.yjsy.model.constant.Table;

/**
 * @author xiafan
 */
public class StatusChangeRequestMapper extends CommonRowMapper {

    public StatusChangeRequestMapper(CommonRowMapper parent) {
        super(parent);
        dateFields.put("date",
                column(Table.STATUS_CHANGE_REQUEST, Column.CREATED_AT));
        stringFields.put("minor",
                column(Table.STATUS_CHANGE_TYPE, Column.MINOR));
        stringFields.put("major",
                column(Table.STATUS_CHANGE_TYPE, Column.MAJOR));
    }

}
