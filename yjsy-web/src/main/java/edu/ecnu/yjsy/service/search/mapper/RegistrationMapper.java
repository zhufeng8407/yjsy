package edu.ecnu.yjsy.service.search.mapper;

import edu.ecnu.yjsy.model.constant.Column;

/**
 * @author xiafan
 */
public class RegistrationMapper extends CommonRowMapper {

    public RegistrationMapper(CommonRowMapper parent) {
        super(parent);
        boolFields.put("fee", Column.FEE);
        boolFields.put("checkin", Column.CHECKIN);
        boolFields.put("register", Column.REGISTER);
        doubleFields.put("feePaid", Column.FEE_PAID);
        stringFields.put("year", Column.YEAR);
        longFields.put("term", Column.TERM);
    }
    
}
