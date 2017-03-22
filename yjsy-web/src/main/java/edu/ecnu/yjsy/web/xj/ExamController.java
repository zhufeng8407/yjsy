package edu.ecnu.yjsy.web.xj;

import static edu.ecnu.yjsy.constant.Constant.API_XJ;
import static edu.ecnu.yjsy.constant.Constant.EXAM_COUNT;
import static edu.ecnu.yjsy.constant.Constant.PAGE_PAGE;
import static edu.ecnu.yjsy.constant.Constant.PAGE_PAGE_INT;
import static edu.ecnu.yjsy.constant.Constant.PAGE_SIZE;
import static edu.ecnu.yjsy.constant.Constant.PAGE_SIZE_INT;
import static edu.ecnu.yjsy.constant.QueryParameter.PARAM_PAGE;
import static edu.ecnu.yjsy.constant.QueryParameter.PARAM_PAGE_SIZE;
import static edu.ecnu.yjsy.security.authorization.DomainSecurityExpressionRoot.DELETE_IS_GRANTED_BY_ROLES;
import static edu.ecnu.yjsy.security.authorization.DomainSecurityExpressionRoot.GET_IS_GRANTED_BY_ROLES;
import static edu.ecnu.yjsy.security.authorization.DomainSecurityExpressionRoot.POST_IS_GRANTED_BY_ROLES;
import static edu.ecnu.yjsy.security.authorization.DomainSecurityExpressionRoot.PUT_IS_GRANTED_BY_ROLES;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import edu.ecnu.yjsy.model.student.Exam;
import edu.ecnu.yjsy.service.xj.ExamService;
import net.sf.json.JSONObject;

@RestController
public class ExamController {

    public static final String API_EXAM = API_XJ + "/exams";

    public static final String API_EXAM_PAPER = API_EXAM + "/paper";

    public static final String API_EXAM_ANSWER = API_EXAM + "/answer";

    public static final String API_EXAM_SINGLE = API_EXAM + "/{examId}";

    public static final String API_EXAM_UPLOAD = API_EXAM + "/upload";

    public static final String API_EXAM_ERROR = API_EXAM + "/errors";

    public static final String API_EXAM_STATS = API_EXAM + "/stats";

    public static final String API_EXAM_EXPORT = API_EXAM + "/export";

    private static final Sort SORT_ID_DESC = new Sort(Direction.DESC, "id");

    @Autowired
    private ExamService service;

    @PreAuthorize(POST_IS_GRANTED_BY_ROLES + API_EXAM_UPLOAD + "\")")
    @RequestMapping(value = API_EXAM_UPLOAD, method = POST)
    @ResponseBody
    public Map<String, Object> upload(MultipartHttpServletRequest req)
            throws InvalidFormatException {
        Map<String, Object> result = new HashMap<>();

        try {
            result = service.upload(req.getFile("file").getInputStream());
        } catch (IOException e) {
            result.put("state", 0);
            return result;
        }

        // if some exams are uploaded correctly
        if (Integer.valueOf(result.get("error").toString()) < Integer
                .valueOf(result.get("total").toString())) {
            result.put("exams", getPage(PAGE_PAGE_INT, PAGE_SIZE_INT));
        }

        return result;
    }

    @PreAuthorize(DELETE_IS_GRANTED_BY_ROLES + API_EXAM_SINGLE + "\")")
    @RequestMapping(value = API_EXAM_SINGLE, method = DELETE)
    @ResponseBody
    public Page<Exam> delete(@PathVariable int examId,
            @RequestParam(value = PARAM_PAGE,
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {

        service.delete(examId);
        return this.getPage(page, size);
    }

    @PreAuthorize(DELETE_IS_GRANTED_BY_ROLES + API_EXAM + "\")")
    @RequestMapping(value = API_EXAM, method = DELETE)
    @ResponseBody
    public Page<Exam> delete(@RequestParam long examIds[],
            @RequestParam(value = PARAM_PAGE,
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = PARAM_PAGE_SIZE,
                    defaultValue = PAGE_SIZE) Integer size) {

        List<Exam> exams = new ArrayList<Exam>();
        for (long id : examIds) {
            Exam e = new Exam();
            e.setId(id);
            exams.add(e);
        }
        service.delete(exams);
        return this.getPage(page, size);
    }

    @SuppressWarnings("unchecked")
    @PreAuthorize(PUT_IS_GRANTED_BY_ROLES + API_EXAM + "\")")
    @RequestMapping(value = API_EXAM, method = PUT)
    @ResponseBody
    public void update(@RequestParam String exam) {
        Map<String, Object> examObj = (Map<String, Object>) JSONObject
                .toBean(JSONObject.fromObject(exam), Map.class);
        Exam theExam = new Exam(examObj.get("test").toString(),
                examObj.get("op1").toString(), examObj.get("op2").toString(),
                examObj.get("op3").toString(), examObj.get("op4").toString(),
                examObj.get("answer").toString(),
                Long.parseLong(examObj.get("total").toString()),
                Long.parseLong(examObj.get("totalCorrect").toString()));
        theExam.setId(Long.parseLong(examObj.get("id").toString()));
        service.save(theExam);
    }

    @PreAuthorize(POST_IS_GRANTED_BY_ROLES + API_EXAM + "\")")
    @RequestMapping(value = API_EXAM, method = POST)
    @ResponseBody
    public Page<Exam> add(@RequestParam String exam,
            @RequestParam(value = PARAM_PAGE,
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = PARAM_PAGE_SIZE,
                    defaultValue = PAGE_SIZE) Integer size) {

        Exam theExam = (Exam) JSONObject.toBean(JSONObject.fromObject(exam),
                Exam.class);
        theExam.setTotal(0);
        theExam.settotalCorrect(0);
        service.save(theExam);

        return this.getPage(page, size);
    }

    @PreAuthorize(GET_IS_GRANTED_BY_ROLES + API_EXAM + "\")")
    @RequestMapping(value = API_EXAM, method = GET)
    @ResponseBody
    public Page<Exam> getByPage(
            @RequestParam(value = PARAM_PAGE,
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = PARAM_PAGE_SIZE,
                    defaultValue = PAGE_SIZE) Integer size) {

        return this.getPage(page, size);
    }

    // @PreAuthorize(GET_HAS_ACCESS_TO_STUDENT + API_EXAM_PAPER + "\",#sno)")
    @RequestMapping(value = API_EXAM_PAPER, method = GET)
    @ResponseBody
    public List<Exam> getPaper(HttpServletRequest request) {

        List<Exam> paper = service.getPaper(Integer.valueOf(EXAM_COUNT));

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("questions", paper);

        return paper;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = API_EXAM_ANSWER, method = POST)
    @ResponseBody
    public int submitAnswer(HttpServletRequest request,
            @RequestParam(value = "answer") String answer[]) {

        List<Exam> questions = (List<Exam>) request.getSession()
                .getAttribute("questions");
        return service.score(answer, questions);
    }

    /**
     * Export exams as an excel file.
     *
     * @param title
     *            the title of the exported exams
     * @param exported
     *            <code>1</code> if a column will be exported
     * @param ids
     *            the exam id
     * @return the excel file of exams
     * @throws IOException
     */
    @PreAuthorize(GET_IS_GRANTED_BY_ROLES + API_EXAM_EXPORT + "\")")
    @RequestMapping(value = API_EXAM_EXPORT, method = GET)
    @ResponseBody
    public byte[] export(
            @RequestParam(value = "title",
                    defaultValue = "题目,A,B,C,D,答案,答题数,正确数,正确率,错误原因") String[] title,
            @RequestParam(value = "exported",
                    defaultValue = "1,1,1,1,1,1,1,1,1,0") int[] exported,
            @RequestParam(value = "ids", defaultValue = "") Integer[] ids)
            throws IOException {
        return service.export(ids, exported, title, null);
    }

    @SuppressWarnings("unchecked")
    @PreAuthorize(GET_IS_GRANTED_BY_ROLES + API_EXAM_ERROR + "\")")
    @RequestMapping(value = API_EXAM_ERROR, method = GET)
    @ResponseBody
    public byte[] exportError(HttpServletRequest request) throws IOException {
        String[] title = { "题目", "A", "B", "C", "D", "答案", "答题数", "正确数", "正确率",
                "错误原因" };
        int[] exported = { 1, 1, 1, 1, 1, 1, 0, 0, 0, 1 };
        HttpSession session = request.getSession();
        return service.export(null, exported, title,
                (Map<String, Object>) session.getAttribute("error"));
    }

    @PreAuthorize(GET_IS_GRANTED_BY_ROLES + API_EXAM_STATS + "\")")
    @RequestMapping(value = API_EXAM_STATS, method = GET)
    @ResponseBody
    public Map<String, Object> statsAccuracy() {
        return service.statsAccuracy();
    }

    // ----------------------------

    private Page<Exam> getPage(int page, int size) {
        return service.findAll(new PageRequest(page, size, SORT_ID_DESC));
    }

}
