package edu.ecnu.yjsy.service.search.processor;

import edu.ecnu.yjsy.model.constant.Column;
import edu.ecnu.yjsy.model.constant.Table;
import edu.ecnu.yjsy.service.search.SearchContext;
import edu.ecnu.yjsy.service.search.SearchSQLService;
import edu.ecnu.yjsy.service.search.mapper.UnitMapper;

import java.util.Map;

import static edu.ecnu.yjsy.constant.QueryParameter.DEPARTMENT_CODE;
import static edu.ecnu.yjsy.constant.QueryParameter.SCHOOL_CODE;
import static edu.ecnu.yjsy.model.constant.Column.META_DEPARTMENT_CODE;
import static edu.ecnu.yjsy.model.constant.Column.META_SCHOOL_CODE;
import static edu.ecnu.yjsy.model.constant.Table.STUDENT;
import static edu.ecnu.yjsy.model.constant.Table.UNIT;

/**
 * 处理查询条件中与院系相关的部分
 *
 * @author xiafan
 */
public class UnitProcessor extends ISearchProcessor {

    @Override
    public void process(Map<String, Object> searchConditions,
                        SearchContext context) {
        backwardProcess(searchConditions, context);
        if (searchConditions.get(SCHOOL_CODE) != null && !searchConditions
                .get(SCHOOL_CODE).toString()
                .equals(SearchSQLService.STRING_FOR_WHOLE_DATA)) {
            context.stringEqual(UNIT, META_SCHOOL_CODE,
                    searchConditions.get(SCHOOL_CODE).toString());
        }

        if (searchConditions.get(DEPARTMENT_CODE) != null && !searchConditions
                .get(DEPARTMENT_CODE).toString()
                .equals(SearchSQLService.STRING_FOR_WHOLE_DATA)) {
            context.stringEqual(UNIT, META_DEPARTMENT_CODE,
                    searchConditions.get(DEPARTMENT_CODE).toString());
        }
    }

    @Override
    protected void generateSearchContext(SearchContext context) {
        if (preProcessor != null) {
            preProcessor.generateSearchContext(context);
            context.join(UNIT, Column.ID, STUDENT, Column.UNIT);
        } else {
            context.setPrimaryTable(Table.UNIT);
        }

        context.addColumn(Table.UNIT, Column.META_SCHOOL);
        context.addColumn(Table.UNIT, Column.META_DEPARTMENT);
        context.setMapper(new UnitMapper(context.getMapper()));
    }
}
