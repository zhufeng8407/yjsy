package edu.ecnu.yjsy.service.search.processor;

import edu.ecnu.yjsy.model.constant.Column;
import edu.ecnu.yjsy.model.constant.Table;
import edu.ecnu.yjsy.service.search.SearchContext;

import java.util.Map;

/**
 * 查询权限数据
 *
 * @author xiafan
 */
public class AccountPrivilegeProcessor extends ISearchProcessor {
    @Override
    public void process(Map<String, Object> searchConditions,
            SearchContext context) {

    }

    @Override
    protected void generateSearchContext(SearchContext context) {
        if (preProcessor != null) {
            preProcessor.generateSearchContext(context);
        }

        context.join(Table.ACCOUNT_PRIVILEGE,
                Column.ACCOUNT_PRIVILEGE_ACCOUNT, Table.ACCOUNT, Column.ID);
    }
}
