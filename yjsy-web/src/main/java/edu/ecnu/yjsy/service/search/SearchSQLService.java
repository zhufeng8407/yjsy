package edu.ecnu.yjsy.service.search;

import edu.ecnu.yjsy.service.search.processor.DisciplineProcessor;
import edu.ecnu.yjsy.service.search.processor.LimitClauseProcessor;
import edu.ecnu.yjsy.service.search.processor.StudentProcessor;
import edu.ecnu.yjsy.service.search.processor.UnitProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * 基于前端传回的搜索条件,构建相应的SQL语句
 *
 * @author xiafan
 */
@Service
public class SearchSQLService {
    public static final String STRING_FOR_WHOLE_DATA = "所有";

    private SearchProcessorBuilder builder;

    private JdbcTemplate jt;

    @Autowired
    public void initialize(JdbcTemplate jt) {
        this.jt = jt;
        builder = new SearchProcessorBuilder(jt, null)
                .addProcessor(new StudentProcessor())//学籍表的搜索
                .addProcessor(new UnitProcessor())//院系的搜索
                .addProcessor(new DisciplineProcessor())
                .addProcessor(new LimitClauseProcessor());//专业的搜索
    }

    /**
     * 创建一个没有添加任何处理链为空的<code>SearchProcessorBuilder</code>
     *
     * @return
     */
    public SearchProcessorBuilder build() {
        return new SearchProcessorBuilder(jt, null);
    }

    public SearchProcessorBuilder defaultSearch() {
        return new SearchProcessorBuilder(builder);
    }

}