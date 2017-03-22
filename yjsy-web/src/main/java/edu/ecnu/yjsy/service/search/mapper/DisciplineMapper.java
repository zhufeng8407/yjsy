package edu.ecnu.yjsy.service.search.mapper;

import edu.ecnu.yjsy.model.constant.Column;

/**
 * 抽取学科相关信息
 *
 * @author xiafan
 */
public class DisciplineMapper extends CommonRowMapper {

    public DisciplineMapper(CommonRowMapper parent) {
        super(parent);
        stringFields.put("category", Column.META_CATEGORY);
        stringFields.put("major", Column.META_MAJOR);
        stringFields.put("minor", Column.META_MINOR);
    }

}
