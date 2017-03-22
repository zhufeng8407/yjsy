package edu.ecnu.yjsy.service.xj;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.ecnu.yjsy.model.student.Exam;
import edu.ecnu.yjsy.model.student.ExamRepository;
import edu.ecnu.yjsy.service.BaseService;
import edu.ecnu.yjsy.service.search.SearchSQLService;

// FIXME
// need to consider the examples from Teacher Xiao

@Service
public class ExamService extends BaseService {

    private static final String[] FILE_FIELDS = { "题目", "A", "B", "C", "D",
            "答案" };

    @Autowired
    private ExamRepository repo;

    @Transactional
    public Map<String, Object> upload(InputStream is)
            throws InvalidFormatException, IOException {
        Map<String, Object> res;
        int errorNumber = 0;
        try {
            Workbook workbook = createWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            res = new HashMap<>();
            stateStore.setState("progress", 5);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row r = sheet.getRow(i);
                Set<String> options = new HashSet<>();
                options.add(getCellValue(r, 1));
                options.add(getCellValue(r, 2));
                options.add(getCellValue(r, 3));
                options.add(getCellValue(r, 4));
                if (options.size() < 4) {
                    errorNumber++;
                    handleErrorData(r, FILE_FIELDS, "exam", "");
                    continue;
                }

                if (r.getCell(0) == null || repo
                        .findAllByTest(r.getCell(0).toString()).size() > 0) {
                    errorNumber++;
                    handleErrorData(r, FILE_FIELDS, "exam", "");
                    continue;
                }

                Exam exam = new Exam(r.getCell(0).toString(),
                        r.getCell(1).toString(), r.getCell(2).toString(),
                        r.getCell(3).toString(), r.getCell(4).toString(),
                        r.getCell(5).toString(), 0, 0);
                repo.save(exam);
                stateStore.setState("progress",
                        5 + i * 95 / sheet.getLastRowNum());
            }

            res.put("total", sheet.getLastRowNum());
            res.put("key", "exam");
            res.put("error", errorNumber);
            if (errorNumber != 0) res.put("state", 1);

        } finally {
            close(is);
        }

        return res;
    }

    public void save(Exam exam) {
        repo.save(exam);
    }

    public Page<Exam> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public void delete(long id) {
        repo.deleteById(id);
    }

    public void delete(List<Exam> exams) {
        repo.delete(exams);
    }

    // ------------------------------

    public List<Exam> getPaper(Integer count) {
        List<Exam> exams = null;
        int diff = (int) repo.count() - count;
        if (diff < 0) {
            exams = repo.findAll();
            count = exams.size();
        } else {
            int begin = (int) (Math.random() * diff);
            exams = repo.findExamsByRange(begin, begin + count);
        }

        List<Exam> paper = new ArrayList<Exam>();
        for (int i = 0; i < count; i++) {
            int j = (int) (Math.random() * exams.size());
            paper.add(exams.get(j));
            exams.remove(j);
        }

        return paper;
    }

    public int score(String answers[], List<Exam> questions) {
        int score = 0;
        for (int i = 0; i < answers.length; i++) {
            questions.get(i).setTotal(questions.get(i).getTotal() + 1);
            if (questions.get(i).getAnswer().equals(answers[i])) {
                score++;
                questions.get(i).settotalCorrect(
                        questions.get(i).gettotalCorrect() + 1);
            }
        }
        repo.save(questions);

        return score;
    }

    @SuppressWarnings("unchecked")
    public byte[] export(Integer[] ids, int[] exported, String[] title,
            Map<String, Object> error) throws IOException {
        List<Exam> exams = new ArrayList<>();
        List<String> errorMsgs = new ArrayList<>();

        if (error == null) {
            if (ids == null || ids.length == 0)
                exams = repo.findAll();
            else {
                List<Integer> theIds = new ArrayList<Integer>();
                for (int i = 0; i < ids.length; i++)
                    theIds.add(ids[i]);
                exams = repo.findAll(theIds);
            }
        } else {
            exams = (List<Exam>) error.get("errors");
            errorMsgs = (List<String>) error.get("errormsg");
        }

        XSSFWorkbook book = new XSSFWorkbook();
        XSSFSheet sheet = book.createSheet("sheet1");
        XSSFRow header = sheet.createRow(0);

        int j = 0;
        for (int i = 0; i < title.length; i++) {
            if (exported[i] == 1) header.createCell(j++).setCellValue(title[i]);
        }

        int rowNum = 1;
        for (Exam exam : exams) {
            j = 0;
            XSSFRow row = sheet.createRow(rowNum++);
            if (exported[0] == 1) {
                row.createCell(j++).setCellValue(exam.getTest());
            }
            if (exported[1] == 1) {
                row.createCell(j++).setCellValue(exam.getOp1());
            }
            if (exported[2] == 1) {
                row.createCell(j++).setCellValue(exam.getOp2());
            }
            if (exported[3] == 1) {
                row.createCell(j++).setCellValue(exam.getOp3());
            }
            if (exported[4] == 1) {
                row.createCell(j++).setCellValue(exam.getOp4());
            }
            if (exported[5] == 1) {
                row.createCell(j++).setCellValue(exam.getAnswer());
            }
            if (exported[6] == 1) {
                row.createCell(j++).setCellValue(exam.getTotal());
            }
            if (exported[7] == 1) {
                row.createCell(j++).setCellValue(exam.gettotalCorrect());
            }
            if (exported[8] == 1) {
                if (exam.getTotal() != 0) {
                    row.createCell(j++).setCellValue(
                            (double) exam.gettotalCorrect() / exam.getTotal());
                } else {
                    row.createCell(j++).setCellValue(0);
                }
            }
            if (exported[9] == 1) {
                row.createCell(j)
                        .setCellValue(errorMsgs.get(rowNum - 2).toString());
            }
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        book.write(output);
        return output.toByteArray();
    }

    public Map<String, Object> statsAccuracy() {
        Map<String, Object> result = new HashMap<>();

        Map<String, Object> xAxis = new HashMap<>();
        Map<String, Object> yAxis = new HashMap<>();
        Map<String, Object> series = new HashMap<>();

        result.put("title", "考题正确率统计");

        yAxis.put("name", "正确率");
        yAxis.put("min", 0);
        yAxis.put("max", 100);
        yAxis.put("interval", 10);
        yAxis.put("formatter", "{value} %");

        List<String> xdata = new ArrayList<String>();
        List<String> test = new ArrayList<String>();
        List<Double> data = new ArrayList<Double>();

        List<Exam> exams = repo.findAll();
        for (int i = 1; i <= exams.size(); i++) {
            xdata.add("第" + i + "题");
            if (exams.get(i - 1).getTotal() != 0)
                data.add((double) exams.get(i - 1).gettotalCorrect()
                        / exams.get(i - 1).getTotal() * 100);
            else
                data.add(0.0);
            test.add(exams.get(i - 1).getTest());
        }
        xAxis.put("data", xdata);

        series.put("data", data);
        series.put("name", "正确率");

        result.put("yAxis", yAxis);
        result.put("xAxis", xAxis);
        result.put("series", series);
        result.put("test", test);

        return result;
    }

    @Override
    public void initConditionSearch(SearchSQLService searchSQLService) {
        // TODO Auto-generated method stub
    }

}
