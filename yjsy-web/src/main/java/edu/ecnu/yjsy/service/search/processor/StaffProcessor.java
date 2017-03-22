package edu.ecnu.yjsy.service.search.processor;

import edu.ecnu.yjsy.model.constant.Column;
import edu.ecnu.yjsy.model.constant.Table;
import edu.ecnu.yjsy.service.search.SearchContext;
import edu.ecnu.yjsy.service.search.mapper.StaffMapper;

import java.util.Map;

/**
 * 搜索Staff数据或者基于Staff数据搜索Account数据
 *
 * @author xiafan
 */
public class StaffProcessor extends ISearchProcessor {
    @Override
    public void process(Map<String, Object> searchConditions,
                        SearchContext context) {
        if (searchConditions.get("name") != null && !searchConditions
                .get("name").toString().isEmpty() && !searchConditions
                .get("name").toString().equals(""))
            context.stringEqual(Table.STAFF, Column.STAFF_NAME,
                    searchConditions.remove("name").toString());

        if (searchConditions.get("sno") != null && !searchConditions.get("sno")
                .toString().isEmpty() && !searchConditions.get("sno").toString()
                .equals(""))
            context.stringEqual(Table.STAFF, Column.STAFF_SNO,
                    searchConditions.remove("sno").toString());

    }

    @Override
    protected void generateSearchContext(SearchContext context) {
        if (preProcessor != null) {
            context.join(Table.STAFF, Column.ID, Table.ACCOUNT,
                    Column.ACCOUNT_STAFF);
        } else {
            context.setPrimaryTable(Table.STAFF);
        }
        context.addColumn(Table.STAFF, Column.STAFF_NAME);
        context.addColumn(Table.STAFF, Column.STAFF_SNO);
        context.addColumn(Table.STAFF, Column.ID);
        context.setMapper(new StaffMapper(context.getMapper()));
    }
}
