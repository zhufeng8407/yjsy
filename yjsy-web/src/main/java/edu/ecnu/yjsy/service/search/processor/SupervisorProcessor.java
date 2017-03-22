package edu.ecnu.yjsy.service.search.processor;

import static edu.ecnu.yjsy.constant.QueryParameter.STAFF_NO;

import java.util.Map;

import edu.ecnu.yjsy.model.constant.Column;
import edu.ecnu.yjsy.model.constant.Table;
import edu.ecnu.yjsy.service.search.SearchContext;

public class SupervisorProcessor extends ISearchProcessor{
    @Override
    public void process(Map<String, Object> searchConditions,
                        SearchContext context) {
        backwardProcess(searchConditions, context);
        if (searchConditions.get(STAFF_NO) != null && !searchConditions.get(STAFF_NO)
                .toString().isEmpty() && !searchConditions.get(STAFF_NO).toString()
                .equals("")) {
            context.join(Table.STAFF, "supervior", Column.ID,  Table.STUDENT,
                    Column.SUPERVISOR);
            context.stringEqual("supervior", Column.STAFF_SNO,
                    searchConditions.remove(STAFF_NO).toString());
        }
    }

    @Override
    protected void generateSearchContext(SearchContext context) {
        assert (preProcessor != null);
        preProcessor.generateSearchContext(context);
    }
}
