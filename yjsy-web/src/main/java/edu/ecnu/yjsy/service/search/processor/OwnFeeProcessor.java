package edu.ecnu.yjsy.service.search.processor;

import edu.ecnu.yjsy.model.constant.Column;
import edu.ecnu.yjsy.model.constant.Table;
import edu.ecnu.yjsy.service.search.SearchContext;
import edu.ecnu.yjsy.service.search.mapper.FeeMapper;

import java.util.Map;

/**
 * @author Bing
 */
public class OwnFeeProcessor extends ISearchProcessor{
    @Override
    public void process(Map<String, Object> searchConditions, SearchContext context) {
        backwardProcess(searchConditions, context);

        //是否欠费
        Object isOwn = searchConditions.remove("isOwnFee");
        if (isOwn != null){
            switch (isOwn.toString()){
                case "0":
                    context.numericEqual(Table.TEMPORARY_FEE, Column.TEMPORARY_OWN_PAY, "0");
                    break;
                case "1":
                    context.numericMoreThan(Table.TEMPORARY_FEE, Column.TEMPORARY_SHOULD_PAY, Table.TEMPORARY_FEE, Column.TEMPORARY_PAY);
                    break;
            }
        }

    }

    @Override
    protected void generateSearchContext(SearchContext context) {
        if (preProcessor != null) preProcessor.generateSearchContext(context);

		//将欠费总额，应缴总额，实缴总额加入 Columns
        context.addColumn(Table.TEMPORARY_FEE, Column.TEMPORARY_OWN_PAY);
        context.addColumn(Table.TEMPORARY_FEE, Column.TEMPORARY_PAY);
        context.addColumn(Table.TEMPORARY_FEE, Column.TEMPORARY_SHOULD_PAY);

        context.setMapper(new FeeMapper(context.getMapper()));
		
		//流水表按照学生 id 进行 group by 操作，然后计算出该学生所有学年的欠费总额。
        String tempTable = "(select " + Column.STUDENT + ", " + "sum(" + Column.FEE_SHOULD_PAID + "-" + Column.FEE_PAID + ") as " + Column.TEMPORARY_OWN_PAY + ", sum(" + Column.FEE_SHOULD_PAID + ") as " + Column.TEMPORARY_SHOULD_PAY + ", sum(" + Column.FEE_PAID + ") as " + Column.TEMPORARY_PAY + " from " + Table.REGISTRATION + " group by " + Column.STUDENT +")";
        context.join(tempTable, Table.TEMPORARY_FEE, Column.STUDENT, Table.STUDENT , Column.ID);

    }
}
