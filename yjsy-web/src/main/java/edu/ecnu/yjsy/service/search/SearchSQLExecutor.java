package edu.ecnu.yjsy.service.search;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 具体根据<code>ISearchProcessor</code>处理得到的<code>SearchContext</code>，组装SQL语句，并且执行的类
 *
 * @author xiafan
 */
public class SearchSQLExecutor {
    final SearchProcessorBuilder builder;
    final SearchContext staticContext;
    final JdbcTemplate jt;

    SearchSQLExecutor(SearchProcessorBuilder builder) {
        this.builder = builder;
        staticContext = builder.processor.getStaticSearchContext();
        this.jt = builder.jt;
    }

    private String toSQL(SearchContext runtimeContext) {
        StringBuilder builder = new StringBuilder();

        // 构造select
        builder.append("select ")
                .append(StringUtils.join(staticContext.selectedColumns, ","));
        if (!runtimeContext.getSelectedColumns().isEmpty()) {
            builder.append(",").append(
                    StringUtils.join(runtimeContext.selectedColumns, ","));
        }

        // 构造from
        builder.append(" from ").append(staticContext.getPrimaryTable())
                .append(" ");
        builder.append(StringUtils.join(staticContext.getJoinTables(), " "));
        builder.append(" ");
        if (!runtimeContext.getJoinTables().isEmpty()) {
            builder.append(
                    StringUtils.join(runtimeContext.getJoinTables(), " "));
            builder.append(" ");
        }

        // 构造where
        builder.append("where ");
        boolean whereAdded = false;
        if (!staticContext.getWhere().isEmpty()) {
            if (whereAdded) {
                builder.append(" and ");
            }
            whereAdded = true;
            builder.append(StringUtils.join(staticContext.getWhere(), " and "));
        }

        if (!runtimeContext.getWhere().isEmpty()) {
            if (whereAdded) {
                builder.append(" and ");
            }
            whereAdded = true;
            builder.append(
                    StringUtils.join(runtimeContext.getWhere(), " and "));
        }

        if (!whereAdded) {
            builder.append(" 1=1 ");
        }

        if (!runtimeContext.getOrderBy().isEmpty()) {
            builder.append(" order by ").append(runtimeContext.getOrderBy());
        }

        if (!runtimeContext.getLimit().isEmpty()) {
            builder.append(runtimeContext.getLimit());
        }

        return builder.toString();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List execute(Map<String, Object> searchConditions) {
        SearchContext context = new SearchContext();
        context.setMapper(staticContext.getMapper());
        builder.processor.process(searchConditions, context);
        return jt.query(toSQL(context), context.getMapper());
    }

}