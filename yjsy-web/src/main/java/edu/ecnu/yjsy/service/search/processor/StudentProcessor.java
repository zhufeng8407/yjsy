package edu.ecnu.yjsy.service.search.processor;

import edu.ecnu.yjsy.constant.QueryParameter;
import edu.ecnu.yjsy.model.constant.Column;
import edu.ecnu.yjsy.model.constant.Table;
import edu.ecnu.yjsy.model.student.PregraduationStatus;
import edu.ecnu.yjsy.service.search.SearchContext;
import edu.ecnu.yjsy.service.search.SearchSQLService;
import edu.ecnu.yjsy.service.search.mapper.StudentProfileMapper;

import static edu.ecnu.yjsy.constant.QueryParameter.STAFF_NO;

import java.util.Map;

/**
 * 处理搜索学籍表本身的字段
 *
 * @author xiafan
 */
public class StudentProcessor extends ISearchProcessor {

    @Override
    public void process(Map<String, Object> searchConditions,
                        SearchContext context) {
        backwardProcess(searchConditions, context);

        if (searchConditions.containsKey("isNew")
                && !searchConditions.get("isNew").toString()
                .equals(SearchSQLService.STRING_FOR_WHOLE_DATA)) {
            context.booleanEqual(Table.STUDENT, Column.IS_NEW,
                    searchConditions.remove("isNew").toString());
        }

        if (searchConditions.get("grade") != null
                && !searchConditions.get("grade").toString()
                .equals(SearchSQLService.STRING_FOR_WHOLE_DATA)
                && !"null".equals(searchConditions.get("grade").toString()))
            context.stringEqual(Table.STUDENT, Column.GRADE,
                    searchConditions.remove("grade").toString());

        // FIXME: @linhao 学籍里面存个学期干啥的?
        if (searchConditions.get("totalterm") != null
                && !searchConditions.get("totalterm").toString()
                .equals(SearchSQLService.STRING_FOR_WHOLE_DATA))
            context.stringEqual(Table.STUDENT, Column.TERM,
                    searchConditions.remove("totalterm").toString());

        if (searchConditions.get("name") != null
                && !searchConditions.get("name").toString().isEmpty()
                && !searchConditions.get("name").toString().equals("")
                && !"null".equals(searchConditions.get("name").toString()))
            context.stringEqual(Table.STUDENT, Column.NAME,
                    searchConditions.remove("name").toString());

        if (searchConditions.get("sno") != null
                && !searchConditions.get("sno").toString().isEmpty()
                && !searchConditions.get("sno").toString().equals(""))
            context.stringEqual(Table.STUDENT, Column.SNO,
                    searchConditions.remove("sno").toString());

        if (searchConditions.get(QueryParameter.IS_NEW) != null)
            context.booleanEqual(Table.STUDENT, Column.IS_NEW,
                    searchConditions.remove(QueryParameter.IS_NEW).toString());

        if (searchConditions.get("date1") != null
                && !searchConditions.get("date1").toString()
                .equals(SearchSQLService.STRING_FOR_WHOLE_DATA)
                && searchConditions.get("date2") != null
                && !searchConditions.get("date2").toString()
                .equals(SearchSQLService.STRING_FOR_WHOLE_DATA))
            context.dateBetween(Table.STUDENT, Column.PREGRADUATION_DATE,
                    searchConditions.get("date1").toString().substring(0,
                            searchConditions.get("date1").toString()
                                    .indexOf('T')),
                    searchConditions.get("date2").toString().substring(0,
                            searchConditions.get("date2").toString()
                                    .indexOf('T')));

        if (searchConditions.get("pregraduationStatus") != null
                && !searchConditions.get("pregraduationStatus").toString()
                .equals(SearchSQLService.STRING_FOR_WHOLE_DATA))
            context.numericEqual(Table.STUDENT, Column.PREGRADUATION_STATUS,
                    Integer.toString(PregraduationStatus
                            .valueOf(searchConditions
                                    .remove("pregraduationStatus").toString())
                            .ordinal()));

        // FIXME: 学籍表里面保存的是staff的外键，不是staff的工号
        if (searchConditions.get(STAFF_NO) != null
                && !searchConditions.get(STAFF_NO).toString()
                .equals(SearchSQLService.STRING_FOR_WHOLE_DATA))
            context.numericEqual(Table.STUDENT, Column.SUPERVISOR,
                    Integer.toString(PregraduationStatus.valueOf(
                            searchConditions.remove(STAFF_NO).toString())
                            .ordinal()));
    }

    @Override
    protected void generateSearchContext(SearchContext context) {
        if (preProcessor != null) {
            // 用于异动申请的查找
            preProcessor.generateSearchContext(context);
            context.join(Table.STUDENT, Column.ID, Table.STATUS_CHANGE_REQUEST,
                    Column.STUDENT);
        } else {
            context.setPrimaryTable(Table.STUDENT);
        }

        // 只返回需要的字段

        context.addColumn(Table.STUDENT, Column.ID);
        context.addColumn(Table.STUDENT, Column.NAME);
        context.addColumn(Table.STUDENT, Column.SNO);
        context.addColumn(Table.STUDENT, Column.GRADE);
        context.addColumn(Table.STUDENT, Column.IS_NEW);
        context.addColumn(Table.STUDENT, Column.TERM);
        context.addColumn(Table.STUDENT, Column.PREGRADUATION_STATUS);

        // 学习形式
        context.join(Table.EDUCATION_MODE, Column.ID, Table.STUDENT,
                Column.EDUCATION_MODE);
        context.addColumn(Table.EDUCATION_MODE, Column.META_NAME);

        // 设置抽取字段的CommonMapper类
        context.setMapper(new StudentProfileMapper(context.getMapper()));
    }

}
