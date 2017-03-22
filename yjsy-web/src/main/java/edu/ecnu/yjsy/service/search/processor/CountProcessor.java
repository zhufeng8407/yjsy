package edu.ecnu.yjsy.service.search.processor;

import edu.ecnu.yjsy.service.search.SearchContext;
import edu.ecnu.yjsy.service.search.mapper.CountMapper;

import java.util.Arrays;
import java.util.Map;

/**
 *
 */
public class CountProcessor extends ISearchProcessor {
    @Override
    public void process(Map<String, Object> searchConditions,
                        SearchContext context) {
        //count map和搜索条件无关
        backwardProcess(searchConditions, context);
    }

    @Override
    protected void generateSearchContext(SearchContext context) {
        if (preProcessor != null)
            preProcessor.generateSearchContext(context);
        context.setMapper(new CountMapper(null));
        context.selectCount(Arrays.asList("*"));
    }
}
