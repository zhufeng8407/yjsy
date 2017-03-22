package edu.ecnu.yjsy.service.search.processor;

import edu.ecnu.yjsy.service.search.SearchContext;

import java.util.Map;

/**
 * 处理搜索条件中的limit
 *
 * @author xiafan
 */
public class LimitClauseProcessor extends ISearchProcessor {

    @Override
    public void process(Map<String, Object> searchConditions,
            SearchContext context) {
        backwardProcess(searchConditions, context);

        if (searchConditions.containsKey("begin") && searchConditions
                .containsKey("end")) {
            context.setupLimit(
                    Integer.parseInt(searchConditions.get("begin").toString()),
                    Integer.parseInt(searchConditions.get("end").toString()));
        }
    }

    @Override
    protected void generateSearchContext(SearchContext context) {
        if (preProcessor != null)
            preProcessor.generateSearchContext(context);
    }
}
