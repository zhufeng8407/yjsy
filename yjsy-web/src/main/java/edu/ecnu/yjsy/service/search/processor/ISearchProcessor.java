package edu.ecnu.yjsy.service.search.processor;

import edu.ecnu.yjsy.service.search.SearchContext;

import java.util.Map;

/**
 * 处理前端传回的查询条件,构建相应的查询语句中的各个部分
 *
 * @author xiafan
 */
public abstract class ISearchProcessor {
    //查询处理链上的前一个处理器
    ISearchProcessor preProcessor;

    /**
     * 调用处理链的前一个对象的处理方法
     *
     * @param searchConditions
     * @param context
     */
    public void backwardProcess(Map<String, Object> searchConditions,
                                SearchContext context) {
        if (preProcessor != null)
            preProcessor.process(searchConditions, context);
    }

    /**
     * 处理searchConditions中的条件,存储到context中
     *
     * @param searchConditions
     * @param context
     */
    public abstract void process(Map<String, Object> searchConditions,
                                 SearchContext context);

    public void setPreProcessor(ISearchProcessor preProcessor) {
        this.preProcessor = preProcessor;
    }

    /**
     * 根据当前处理类需要涉及到的SQL操作，设置静态的<code>SearchContext</code>
     *
     * @param context
     */
    protected abstract void generateSearchContext(SearchContext context);

    /**
     * 根据当前处理类需要涉及到的SQL操作，设置静态的<code>SearchContext</code>，例如：设置需要join的表，需要选择的字段等等。
     * 此处静态的涵义是指那些不需要根据搜索条件动态设置的查询操作，例如可能某些查询条件没有出现，就可以不链接某张表，这时就不需要
     * 在当前函数中设置该表的链接操作，而是在process函数中动态设置
     */
    public SearchContext getStaticSearchContext() {
        SearchContext ret = new SearchContext();
        generateSearchContext(ret);
        return ret;
    }
}
