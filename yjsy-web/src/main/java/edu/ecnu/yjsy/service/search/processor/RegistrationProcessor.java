package edu.ecnu.yjsy.service.search.processor;

import edu.ecnu.yjsy.model.constant.Column;
import edu.ecnu.yjsy.model.constant.Table;
import edu.ecnu.yjsy.service.search.SearchContext;
import edu.ecnu.yjsy.service.search.SearchSQLService;
import edu.ecnu.yjsy.service.search.mapper.RegistrationMapper;

import static edu.ecnu.yjsy.model.constant.Table.REGISTRATION;
import static edu.ecnu.yjsy.model.constant.Table.STUDENT;

import java.util.Map;

/**
 * 处理和报道注册相关的搜索条件
 *
 * @author xiafan
 */
public class RegistrationProcessor extends ISearchProcessor {

    @Override
    public void process(Map<String, Object> searchConditions,
            SearchContext context) {
        backwardProcess(searchConditions, context);

        if (searchConditions.get("regYear") != null
                && !searchConditions.get("regYear").toString()
                        .equals(SearchSQLService.STRING_FOR_WHOLE_DATA)) {
            context.stringEqual(Table.REGISTRATION, Column.YEAR,
                    searchConditions.remove("regYear").toString());
        }

        if (searchConditions.get("term") != null
                && !searchConditions.get("term").toString()
                        .equals(SearchSQLService.STRING_FOR_WHOLE_DATA)) {
            context.numericEqual(Table.REGISTRATION, Column.TERM,
                    searchConditions.remove("term").toString());
        }

        // 是否缴费
        if (searchConditions.get("isFee") != null
                && !searchConditions.get("isFee").toString()
                        .equals(SearchSQLService.STRING_FOR_WHOLE_DATA))
            context.booleanEqual(Table.REGISTRATION, Column.FEE,
                    searchConditions.remove("isFee").toString());

        // 是否报道
        if (searchConditions.get("isCheckin") != null
                && !searchConditions.get("isCheckin").toString()
                        .equals(SearchSQLService.STRING_FOR_WHOLE_DATA))
            context.booleanEqual(Table.REGISTRATION, Column.CHECKIN,
                    searchConditions.remove("isCheckin").toString());
    }

    @Override
    protected void generateSearchContext(SearchContext context) {
        if (preProcessor != null) preProcessor.generateSearchContext(context);

        context.addColumn(Table.REGISTRATION, Column.CHECKIN);
        context.addColumn(Table.REGISTRATION, Column.REGISTER);
        context.addColumn(Table.REGISTRATION, Column.FEE);
        context.addColumn(Table.REGISTRATION, Column.FEE_PAID);
        context.addColumn(Table.REGISTRATION, Column.YEAR);
        context.addColumn(Table.REGISTRATION, Column.TERM);

        context.setMapper(new RegistrationMapper(context.getMapper()));
        context.leftJoin(REGISTRATION, Column.STUDENT, STUDENT, Column.ID);

    }
}
