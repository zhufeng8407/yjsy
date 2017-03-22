package edu.ecnu.yjsy.service;

import static edu.ecnu.yjsy.constant.QueryParameter.PARAM_ITEMS;
import static edu.ecnu.yjsy.constant.QueryParameter.PARAM_ITEMS_COUNT;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.ecnu.yjsy.service.search.SearchSQLExecutor;
import edu.ecnu.yjsy.service.search.SearchSQLService;
import net.sf.json.JSONObject;

/**
 * 增加处理查询的方法。
 * 
 * @author xulinhao
 */

public abstract class HandleSearch extends HandleDate {

    private static final Logger LOG = LoggerFactory
            .getLogger(HandleSearch.class);

    // 根据搜索条件，创建相应的Executor类
    protected SearchSQLExecutor exec;

    protected SearchSQLExecutor countExec;

    /**
     * 如果需要使用搜索服务，则需要通过实现这个函数完成搜索处理类的构造初始化
     *
     * @param searchSQLService
     */
    public abstract void initConditionSearch(SearchSQLService searchSQLService);

    @SuppressWarnings("unchecked")
    public Map<String, Object> conditionSearch(String searchCondition,
            Integer page, Integer size) {
        Map<String, Object> sc = (Map<String, Object>) JSONObject
                .toBean(JSONObject.fromObject(searchCondition), Map.class);
        int actualPage = 0;
        int actualSize = Integer.MAX_VALUE;
        if (page != null) {
            actualPage = page;
            actualSize = size;
        }
        return conditionSearch(sc, actualPage, actualSize);
    }

    @SuppressWarnings("rawtypes")
    public Map<String, Object> conditionSearch(Map<String, Object> sc, int page,
            int size) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> countSc = new HashMap<String, Object>(sc);
        if (size != Integer.MAX_VALUE) {
            int begin = page * size;
            int end = size;
            sc.put("begin", begin);
            sc.put("end", end);
        }

        try {
            List result = exec.execute(sc);
            res.put(PARAM_ITEMS, result);
            if (result.size() > 0) {
                res.put(PARAM_ITEMS_COUNT, countExec.execute(countSc).get(0));
            } else {
                // 减少一次查询？
                res.put(PARAM_ITEMS_COUNT, 0);
            }
        } catch (Exception e) {
            LOG.error("{}", e);
        }
        return res;
    }

}
