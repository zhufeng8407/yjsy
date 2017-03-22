package edu.ecnu.yjsy.service.search.processor;

import edu.ecnu.yjsy.model.constant.Column;
import edu.ecnu.yjsy.model.constant.Table;
import edu.ecnu.yjsy.service.search.SearchContext;

import static edu.ecnu.yjsy.model.constant.Column.CREATED_AT;
import static edu.ecnu.yjsy.model.constant.Table.STATUS_CHANGE_REQUEST;

import java.util.Map;

/**
 * @author xiafan
 */
public class StatusChangeRequestProcessor extends ISearchProcessor {
    @Override
    public void process(Map<String, Object> searchConditions,
                        SearchContext context) {
        backwardProcess(searchConditions, context);

        if (searchConditions.get("dateFrom") != null
                && !searchConditions.get("dateFrom").toString().equals("null")
                && !searchConditions.get("dateFrom").toString().equals(""))
            context.dateGE(STATUS_CHANGE_REQUEST, CREATED_AT,
                    searchConditions.get("dateFrom").toString());
        if (searchConditions.get("dateEnd") != null
                && !searchConditions.get("dateEnd").toString().equals("null")
                && !searchConditions.get("dateEnd").toString().equals(""))
            context.dateLE(STATUS_CHANGE_REQUEST, CREATED_AT,
                    searchConditions.get("dateEnd").toString());
    }

    @Override
    protected void generateSearchContext(SearchContext context) {
        if (preProcessor != null) {
            preProcessor.generateSearchContext(context);
            //这个情况下搜索的是异动审批的数据，需要和异动审批表进行连接
            context.join(STATUS_CHANGE_REQUEST, Column.ID,
                    Table.STATUS_CHANGE_AUDIT, Column.CHANGE_REQUEST);
        } else {
            //这个情况下搜索的是异动申请的数据，需要和异动审批表进行连接
            context.setPrimaryTable(Table.STATUS_CHANGE_REQUEST);
        }
        //找申请类型
        context.join(Table.STATUS_CHANGE_TYPE, Column.ID,
                Table.STATUS_CHANGE_REQUEST, Column.CHANGE_TYPE);
        //找代申请人
        context.leftJoin(Table.STAFF,
                Column.ID, STATUS_CHANGE_REQUEST, Column.STAFF);

    }
}
