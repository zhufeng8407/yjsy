package edu.ecnu.yjsy.service.search;

import edu.ecnu.yjsy.service.search.processor.ISearchProcessor;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 采用build模式构建搜索条件的处理链
 *
 * @author xiafan
 */
public class SearchProcessorBuilder {

    ISearchProcessor processor = null;

    JdbcTemplate jt;

    public SearchProcessorBuilder(SearchProcessorBuilder other) {
        this.processor = other.processor;
        this.jt = other.jt;
    }

    public SearchProcessorBuilder(JdbcTemplate jt,
                                  ISearchProcessor processor) {
        this.jt = jt;
        this.processor = processor;
    }

    public SearchProcessorBuilder addProcessor(
            ISearchProcessor newProcessor) {
        newProcessor.setPreProcessor(processor);
        this.processor = newProcessor;
        return this;
    }

    public SearchSQLExecutor create() {
        return new SearchSQLExecutor(this);
    }

}
