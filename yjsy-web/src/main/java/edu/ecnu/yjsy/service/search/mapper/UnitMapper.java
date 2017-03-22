package edu.ecnu.yjsy.service.search.mapper;

import edu.ecnu.yjsy.model.constant.Column;

/**
 * 抽取院系相关的数据
 *
 * @author xiafan
 */
public class UnitMapper extends CommonRowMapper {

    public UnitMapper(CommonRowMapper parent) {
        super(parent);
        stringFields.put("school", Column.META_SCHOOL);
        stringFields.put("department", Column.META_DEPARTMENT);
    }

}
