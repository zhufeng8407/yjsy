package edu.ecnu.yjsy.service.search;

import edu.ecnu.yjsy.model.constant.Table;
import edu.ecnu.yjsy.service.search.mapper.CommonRowMapper;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 存储搜索的文境
 *
 * @author xiafan
 */
public class SearchContext {
    CommonRowMapper mapper = null;
    Set<String> selectedColumns = new HashSet<>();
    String primaryTable = Table.STUDENT;
    List<String> joinTables = new ArrayList<>();
    Set<String> joinedTable = new HashSet<String>();
    List<String> where = new ArrayList<>();
    String orderBy = "";
    String limit = "";

    /**
     * 返回的数据范围
     *
     * @param begin
     * @param end
     */
    public void setupLimit(int begin, int end) {
        limit = String.format(" limit %d, %d", begin, end);
    }

    public void dateEqual(String table, String column, String value) {
        where.add(
                String.format("unix_timestamp(%s.%s)=unix_timestamp(%s)", table,
                        column, value));
    }

    public void isNull(String table, String column) {
        where.add(String.format("(%s.%s is null or %1$s.%2$s = false)", table, column));
    }

    public void numericEqual(String table, String column, String value) {
        where.add(String.format("%s.%s=%s", table, column, value));
    }

    public void numericEqual(String table, String column, String table2, String column2){
        where.add(String.format("%s.%s=%s.%s", table, column, table2, column2));
    }

    public void numericMoreThan(String table, String column, String table2, String column2){
        where.add(String.format("%s.%s>%s.%s", table, column, table2, column2));
    }

    public void booleanEqual(String table, String column, String value) {
        if (value.equals("1"))
            value = "true";
        else if (value.equals("0"))
            value = "false";
        where.add(String.format("%s.%s=%s", table, column, value));
    }

    public void stringEqual(String table, String column, String value) {
        where.add(String.format("%s.%s='%s'", table, column, value));
    }

    public void dateGE(String table, String column, String start) {
        where.add(String.format("unix_timestamp(date(%s.%s))>=unix_timestamp('%s')",
                table, column, start));
    }

    public void dateLE(String table, String column, String end) {
        where.add(String.format("unix_timestamp(date(%s.%s))<=unix_timestamp('%s')",
                table, column, end));
    }

    public void dateBetween(String table, String column, String dateStart,
                            String dateEnd) {
        where.add(String.format("%s.%s between '%s' and '%s'", table, column,
                dateStart, dateEnd));
    }

    public CommonRowMapper getMapper() {
        return mapper;
    }

    public void setMapper(CommonRowMapper mapper) {
        this.mapper = mapper;
    }

    public void selectCount(List<String> columns) {
        selectedColumns.clear();
        selectedColumns.add(String
                .format("count(%s)", StringUtils.join(columns, ",")));
    }

    public void setPrimaryTable(String primaryTable) {
        this.primaryTable = primaryTable;
    }

    public void addColumn(String table, String column) {
        selectedColumns.add(String.format("%s.%s", table, column));
    }

    public void addColumn(String column){
        selectedColumns.add(column);
    }

    public void addColumnAs(String table, String column, String alias) {
        selectedColumns.add(String.format("%s.%s as %s", table, column, alias));
    }

    /**
     * @param table     新加入的表
     * @param column
     * @param joinTable 已有的表
     * @param joinCol
     */
    public void leftJoin(String table, String column, String joinTable,
                         String joinCol) {
        if (!joinedTable.contains(table)) {
            joinedTable.add(table);
            joinTables.add(String
                    .format("left join %s on %1$s.%s = %s.%s", table, column,
                            joinTable, joinCol));
        }
    }

    /**
     * @param table     新加入的表
     * @param column
     * @param joinTable 已有的表
     * @param joinCol
     */
    public void join(String table, String column, String joinTable,
                     String joinCol) {
        if (!joinedTable.contains(table)) {
            joinedTable.add(table);
            joinTables.add(String
                    .format("join %s on %1$s.%s = %s.%s", table, column,
                            joinTable, joinCol));
        }
    }

    /**
     * @param table     新加入的表
     * @param column
     * @param joinTable 已有的表
     * @param joinCol
     */
    public void join(String table, String tableAlias, String column,
                     String joinTable, String joinCol) {
        if (!joinedTable.contains(table)) {
            joinedTable.add(tableAlias);
            joinTables.add(String
                    .format("join %s as %s on %2$s.%s = %s.%s", table,
                            tableAlias, column, joinTable, joinCol));
        }
    }

    public Set<String> getSelectedColumns() {
        return selectedColumns;
    }

    public String getPrimaryTable() {
        return primaryTable;
    }

    public List<String> getJoinTables() {
        return joinTables;
    }

    public List<String> getWhere() {
        return where;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public String getLimit() {
        return limit;
    }

}
