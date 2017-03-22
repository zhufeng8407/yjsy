package edu.ecnu.yjsy.service.search.processor;

import edu.ecnu.yjsy.model.constant.Column;
import edu.ecnu.yjsy.model.constant.Table;
import edu.ecnu.yjsy.service.search.SearchContext;
import edu.ecnu.yjsy.service.search.SearchSQLService;
import edu.ecnu.yjsy.service.search.mapper.DisciplineMapper;

import java.util.Map;

/**
 * 根据学生当前的学科信息进行搜索
 *
 * @author xiafan
 */
public class DisciplineProcessor extends ISearchProcessor {

    @Override
    public void process(Map<String, Object> searchConditions,
            SearchContext context) {
        backwardProcess(searchConditions, context);

        if (searchConditions.get("discipline") != null && !searchConditions
                .get("discipline").toString()
                .equals(SearchSQLService.STRING_FOR_WHOLE_DATA))
            context.stringEqual(Table.DISCIPLINE, Column.META_CATEGORY,
                    searchConditions.remove("discipline").toString());

        if (searchConditions.get("majorCode") != null && !searchConditions
                .get("majorCode").toString()
                .equals(SearchSQLService.STRING_FOR_WHOLE_DATA))
            context.stringEqual(Table.DISCIPLINE, Column.META_MAJOR_CODE,
                    searchConditions.remove("majorCode").toString());

        if (searchConditions.get("minorCode") != null && !searchConditions
                .get("minorCode").toString()
                .equals(SearchSQLService.STRING_FOR_WHOLE_DATA))
            context.stringEqual(Table.DISCIPLINE, Column.META_CATEGORY,
                    searchConditions.remove("minorCode").toString());
    }

    @Override
    protected void generateSearchContext(SearchContext context) {
        if (preProcessor != null)
            preProcessor.generateSearchContext(context);
        context.setMapper(new DisciplineMapper(context.getMapper()));
        context.join(Table.DISCIPLINE, Column.ID, Table.STUDENT,
                Column.DISCIPLINE);
        context.addColumn(Table.DISCIPLINE, Column.META_CATEGORY);
        context.addColumn(Table.DISCIPLINE, Column.META_MAJOR);
        context.addColumn(Table.DISCIPLINE, Column.META_MINOR);

    }

}
