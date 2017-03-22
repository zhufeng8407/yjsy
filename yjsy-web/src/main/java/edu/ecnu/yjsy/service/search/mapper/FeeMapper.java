package edu.ecnu.yjsy.service.search.mapper;

import edu.ecnu.yjsy.model.constant.Column;

/**
 * @author Bing
 */
public class FeeMapper extends CommonRowMapper {

    public FeeMapper(CommonRowMapper parent) {
        super(parent);

        doubleFields.put("feePaid", Column.FEE_PAID);
        doubleFields.put("feeShouldPaid", Column.FEE_SHOULD_PAID);
        doubleFields.put("feeOwn", Column.FEE_OWN);

    }
}
