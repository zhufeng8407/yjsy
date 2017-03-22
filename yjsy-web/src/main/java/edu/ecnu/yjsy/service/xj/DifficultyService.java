package edu.ecnu.yjsy.service.xj;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.ecnu.yjsy.model.student.Difficulty;
import edu.ecnu.yjsy.model.student.DifficultyRepository;
import edu.ecnu.yjsy.model.student.Student;
import edu.ecnu.yjsy.model.student.StudentRepository;
import edu.ecnu.yjsy.service.BaseService;
import edu.ecnu.yjsy.service.search.SearchProcessorBuilder;
import edu.ecnu.yjsy.service.search.SearchSQLService;
import edu.ecnu.yjsy.service.search.processor.CountProcessor;
import edu.ecnu.yjsy.service.search.processor.DifficultyProcessor;
import edu.ecnu.yjsy.service.search.processor.DisciplineProcessor;
import edu.ecnu.yjsy.service.search.processor.LimitClauseProcessor;
import edu.ecnu.yjsy.service.search.processor.StudentProcessor;
import edu.ecnu.yjsy.service.search.processor.UnitProcessor;

/**
 * 困难认定包括2个模块：
 * <p>
 * 模块一：困难生名单导入 以文件形式批量导入
 * <p>
 * 模块二：困难名单查询
 * <p>
 * 1.查询条件包括：学号，学院，专业，年级，是否困难 ，展示查询结果：序号，学号，姓名，学院，专业，年级，（这里只查贫困生）
 * <p>
 * 2. 可以做修改贫困生年级和删除记录得操作，更新表
 *
 * @author PYH
 */
@Service
public class DifficultyService extends BaseService {

    private static final String[] FILE_FIELDS = { "学号", "学年", "是否困难" };

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private DifficultyRepository difficultyRepo;

    @Autowired
    public void initConditionSearch(SearchSQLService searchSQLService) {
        SearchProcessorBuilder processorBuilder = searchSQLService.build()
                .addProcessor(new StudentProcessor())// 学籍表的搜索
                .addProcessor(new DifficultyProcessor())
                .addProcessor(new UnitProcessor())// 院系的搜索
                .addProcessor(new DisciplineProcessor())
                .addProcessor(new LimitClauseProcessor());// 专业的搜索;

        exec = processorBuilder.create();
        countExec = new SearchProcessorBuilder(processorBuilder)
                .addProcessor(new CountProcessor()).create();
    }

    /**
     * 获取年级
     *
     * @return
     */
    public List<String> getDiffYear() {
        return difficultyRepo.getYear();
    }

    @Transactional
    public Map<String, Object> upload(InputStream is, String errorType)
            throws InvalidFormatException, IOException {
        try {
            stateStore.setState(PROGRESS_FIELD, 5);

            Workbook workbook = createWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);

            Map<String, Object> result = new HashMap<>();

            // 检查文件格式
            Row titles = sheet.getRow(0);
            if (!checkFormat(titles, FILE_FIELDS)) {
                result.put("fileFormat", false);
                return result;
            }

            int error = 0;
            result.put("fileFormat", true);
            Map<String, Integer> schema = parseSchema(titles);

            List<Difficulty> difficulties = new ArrayList<>();
            Row row = null;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);
                List<String> values = parseRow(row, FILE_FIELDS, schema);

                String sno = values.get(0);
                String year = values.get(1);
                if (sno == null || year == null) {
                    error++;
                    handleErrorData(row, FILE_FIELDS, errorType, "");
                    continue;
                }

                Student student = studentRepo.findBySno(sno);
                if (student != null) {
                    if (difficultyRepo.findByStudentAndYear(student,
                            year) == null) {
                        difficulties.add(new Difficulty(year,
                                parseBoolean(values.get(2)), student));
                    } else {
                        error++;
                        handleErrorData(row, FILE_FIELDS, errorType, "");
                    }
                } else {
                    error++;
                    handleErrorData(row, FILE_FIELDS, errorType, "");
                }
                stateStore.setState(PROGRESS_FIELD,
                        i * 90 / sheet.getLastRowNum());
            }
            difficultyRepo.save(difficulties);

            result.put("totalcount", sheet.getLastRowNum());
            result.put("errorcount", error);
            stateStore.setState(PROGRESS_FIELD, 100);

            return result;
        } finally {
            close(is);
        }
    }

    public void delete(long id) {
        difficultyRepo.deleteById(id);
    }

    public boolean update(long id, String year, String isDifficulty) {
        Difficulty difficulty = difficultyRepo.findOne(id);
        // 暂时认为只会修改学年,因为贫困只有是和否
        difficulty.setYear(year);
        difficulty.setDifficulty(parseBoolean(isDifficulty));
        difficultyRepo.save(difficulty);
        return true;
    }

}
