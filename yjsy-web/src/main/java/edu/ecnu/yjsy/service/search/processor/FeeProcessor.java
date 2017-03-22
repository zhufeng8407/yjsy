package edu.ecnu.yjsy.service.search.processor;

import edu.ecnu.yjsy.model.constant.Column;
import edu.ecnu.yjsy.model.constant.Table;
import edu.ecnu.yjsy.service.search.SearchContext;
import edu.ecnu.yjsy.service.search.mapper.FeeMapper;

import java.util.Map;

/**
 * @author Bing
 */
public class FeeProcessor extends ISearchProcessor {

    @Override
    public void process(Map<String, Object> searchConditions,
            SearchContext context) {
        backwardProcess(searchConditions, context);

        // 是否欠费
        Object isOwn = searchConditions.remove("isOwnFee");
        if (isOwn != null) {
            switch (isOwn.toString()) {
            case "0":
                context.numericEqual(Table.REGISTRATION, Column.FEE_PAID,
                        Table.REGISTRATION, Column.FEE_SHOULD_PAID);
                break;
            case "1":
                context.numericMoreThan(Table.REGISTRATION,
                        Column.FEE_SHOULD_PAID, Table.REGISTRATION,
                        Column.FEE_PAID);
                break;
            }
        }
    }

    @Override
    protected void generateSearchContext(SearchContext context) {
        if (preProcessor != null) preProcessor.generateSearchContext(context);

        String column = Column.FEE_SHOULD_PAID + "-" + Column.FEE_PAID + " as "
                + Column.FEE_OWN;

        context.addColumn(column);
        context.addColumn(Table.REGISTRATION, Column.FEE_SHOULD_PAID);
        context.addColumn(Table.REGISTRATION, Column.FEE_PAID);
        context.setMapper(new FeeMapper(context.getMapper()));
    }

}
