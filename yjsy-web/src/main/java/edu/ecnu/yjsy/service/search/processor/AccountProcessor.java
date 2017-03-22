package edu.ecnu.yjsy.service.search.processor;

import edu.ecnu.yjsy.model.constant.Table;
import edu.ecnu.yjsy.service.search.SearchContext;

import java.util.Map;

/**
 * 搜索账号信息
 *
 * @author xiafan
 */
public class AccountProcessor extends ISearchProcessor {
    @Override
    public void process(Map<String, Object> searchConditions,
            SearchContext context) {
        //如果以后需要根据用户名检索,可以在这里添加
    }

    @Override
    protected void generateSearchContext(SearchContext context) {
        assert preProcessor == null;
        //通过staff搜索帐号时，使用Account作为主表
        context.setPrimaryTable(Table.ACCOUNT);
    }
}
