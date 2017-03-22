package edu.ecnu.yjsy.service.search.processor;

import edu.ecnu.yjsy.model.constant.Column;
import edu.ecnu.yjsy.model.constant.Table;
import edu.ecnu.yjsy.service.search.SearchContext;
import edu.ecnu.yjsy.service.search.mapper.StudentProfileMapper;

/**
 * 基于学生搜索帐号，这里处理的是学生的搜索条件
 *@author xiafan
 */
public class AccountStudentProcessor extends StudentProcessor{
    @Override
    protected void generateSearchContext(SearchContext context) {
        assert preProcessor == null;
        //通过staff搜索帐号时，使用Account作为主表
       context.join(Table.STUDENT, Column.ID, Table.ACCOUNT, Column.STUDENT);

        //只返回需要的字段
        context.addColumn(Table.STUDENT, Column.ID);
        context.addColumn(Table.STUDENT, Column.NAME);
        context.addColumn(Table.STUDENT, Column.SNO);
        context.addColumn(Table.STUDENT, Column.GRADE);
        context.addColumn(Table.STUDENT, Column.IS_NEW);
        context.addColumn(Table.STUDENT, Column.TERM);

        //设置抽取字段的CommonMapper类
        context.setMapper(new StudentProfileMapper(context.getMapper()));
    }
}
