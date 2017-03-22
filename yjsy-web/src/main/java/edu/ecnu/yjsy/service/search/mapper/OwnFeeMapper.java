package edu.ecnu.yjsy.service.search.mapper;

import edu.ecnu.yjsy.model.constant.Column;

/**
 * @author Bing
 */
public class OwnFeeMapper extends CommonRowMapper {

    public OwnFeeMapper(CommonRowMapper parent) {
        super(parent);

		//学生总共应缴的学费，实缴学费，欠费额度
        doubleFields.put("allFeePaid", Column.TEMPORARY_PAY);
        doubleFields.put("allFeeShouldPaid", Column.TEMPORARY_SHOULD_PAY);
        doubleFields.put("allFeeOwn", Column.TEMPORARY_OWN_PAY);
    }
}
