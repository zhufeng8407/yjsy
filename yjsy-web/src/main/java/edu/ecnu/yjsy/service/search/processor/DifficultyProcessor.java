package edu.ecnu.yjsy.service.search.processor;

import edu.ecnu.yjsy.model.constant.Column;
import edu.ecnu.yjsy.model.constant.Table;
import edu.ecnu.yjsy.service.search.SearchContext;
import edu.ecnu.yjsy.service.search.SearchSQLService;
import edu.ecnu.yjsy.service.search.mapper.DifficultyMapper;

import java.util.Map;

/**
 * 处理困难相关的搜索条件,当前只能处理搜索学生的情况，不能只搜索困难表
 *
 * @author xiafan
 */
public class DifficultyProcessor extends ISearchProcessor {

    @Override
    public void process(Map<String, Object> searchConditions,
                        SearchContext context) {
        backwardProcess(searchConditions, context);

        if (searchConditions.get("regYear") != null
                && !searchConditions.get("regYear").toString()
                .equals(SearchSQLService.STRING_FOR_WHOLE_DATA)) {
            context.stringEqual(Table.DIFFICULTY, Column.YEAR,
                    searchConditions.remove("regYear").toString());
        }

        if (searchConditions.get("term") != null
                && !searchConditions.get("term").toString()
                .equals(SearchSQLService.STRING_FOR_WHOLE_DATA)) {
            context.numericEqual(Table.DIFFICULTY, Column.YEAR,
                    searchConditions.remove("term").toString());
        }

        String isDiff = searchConditions.remove("isDifficulty").toString();
        if (!isDiff.equals(SearchSQLService.STRING_FOR_WHOLE_DATA)
                && isDiff.equals("1")) {
            context.booleanEqual(Table.DIFFICULTY, Column.LEVEL, "true");
        } else {
            context.isNull(Table.DIFFICULTY, Column.LEVEL);
        }
    }

    @Override
    protected void generateSearchContext(SearchContext context) {
        assert (preProcessor != null);
        // FIXME: preProcessor必须是<code>StudentProcessor</code>
        preProcessor.generateSearchContext(context);
        // 使用左连接，这样即使没有流水记录，也能够查出结果
        context.leftJoin(Table.DIFFICULTY, Column.STUDENT, Table.STUDENT,
                Column.ID);

        context.setMapper(new DifficultyMapper(context.getMapper()));
        context.addColumnAs(Table.DIFFICULTY, Column.ID, DifficultyMapper.DIFF_ID);
        context.addColumn(Table.DIFFICULTY, Column.YEAR);
        context.addColumn(Table.DIFFICULTY, Column.LEVEL);
    }
}
