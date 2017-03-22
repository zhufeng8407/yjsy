package edu.ecnu.yjsy.web.xj;

import static edu.ecnu.yjsy.constant.Constant.*;
import static edu.ecnu.yjsy.security.authorization.DomainSecurityExpressionRoot.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import edu.ecnu.yjsy.exception.BusinessException;
import edu.ecnu.yjsy.model.student.Student;
import edu.ecnu.yjsy.service.xj.RepoService;
import edu.ecnu.yjsy.service.xj.StudentService;

/**
 * @author xiafan
 */
@RestController
public class StudentController {
    public static final String API_STUDENT = API_XJ + "/students";

    public static final String API_STUDENT_SINGLE = API_STUDENT + "/{sno}";

    public static final String API_STUDENT_SINGLE_IMAGE = API_STUDENT_SINGLE
            + "/image";

    public static final String API_STUDENT_SINGLE_CURRENT = API_STUDENT_SINGLE
            + "/current";

    public static final String API_STUDENT_SINGLE_PROFILE = API_STUDENT_SINGLE
            + "/profile";

    public static final String API_STUDENT_SEARCH_PROFILE = API_STUDENT
            + "/profile";

    public static final String API_STUDENT_BATCHUPDATE = API_STUDENT
            + "/batchUpdate";

    public static final String API_STUDENT_UPLOAD_ERROR = API_STUDENT
            + "/error";

    private static final Logger LOG = LoggerFactory
            .getLogger(StudentController.class);

    @Autowired
    private StudentService service;

    @Autowired
    private RepoService repoService;

    /**
     * 增加一个学生
     *
     * @param student
     *            json格式的字符串,用于传递Student中的字段值
     * @return
     */
    @PreAuthorize(POST_IS_GRANTED_BY_ROLES + API_STUDENT + "\")")
    @RequestMapping(value = API_STUDENT, method = POST, params = { "student" })
    @ResponseBody
    public boolean save(@RequestParam String student) {
        try {
            return service.save(null, student);
        } catch (ParseException e) {
            throw new BusinessException(ERROR_DES_DATA_PARSE,
                    ERROR_CODE_DATA_PARSE);
        } catch (IOException e) {
            LOG.error(e.getMessage());
            throw new BusinessException(ERROR_DES_DATA_STORE,
                    ERROR_CODE_DATA_STORE);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new BusinessException(ERROR_DES_DB_CONN_TIMEOUT,
                    ERROR_CODE_DB_CONN_TIMEOUT);
        }
    }

    /**
     * 批量上传导入学生学籍
     *
     * @param req
     * @return
     */
    @PreAuthorize(POST_IS_GRANTED_BY_ROLES + API_STUDENT + "\")")
    @RequestMapping(value = API_STUDENT, method = POST)
    @ResponseBody
    public Map<String, Object> upload(MultipartHttpServletRequest req) {
        // 批量上传前，先处理上一届保留入学资格的新生。
        repoService.updatePastNewStudent();
        try {
            return service.upload(req.getFile("file").getInputStream(),req.getParameter("type"));
        } catch (IOException e) {
            LOG.error("{}", e);
            return null;
        }
    }

    /**
     * 更新学生的信息
     *
     * @param sno
     * @param student
     * @return
     */
    @PreAuthorize(PUT_HAS_ACCESS_TO_STUDENT + API_STUDENT_SINGLE + "\",#sno)")
    @RequestMapping(value = API_STUDENT_SINGLE, method = PUT)
    @ResponseBody
    public boolean save(@PathVariable String sno,
            @RequestParam String student) {
        try {
            return service.save(sno, student);
        } catch (ParseException e) {
            throw new BusinessException(ERROR_DES_DATA_PARSE,
                    ERROR_CODE_DATA_PARSE);
        } catch (IOException e) {
            System.out.println(e);
            throw new BusinessException(ERROR_DES_DATA_STORE,
                    ERROR_CODE_DATA_STORE);
        } catch (Exception e) {
            System.out.println(e);
            throw new BusinessException(ERROR_DES_DB_CONN_TIMEOUT,
                    ERROR_CODE_DB_CONN_TIMEOUT);
        }
    }

    /**
     * @param sno
     *            学号
     * @param req
     * @return
     * @throws IOException
     */
    @PreAuthorize(POST_HAS_ACCESS_TO_STUDENT + API_STUDENT_SINGLE_IMAGE
            + "\",#sno)")
    @RequestMapping(value = API_STUDENT_SINGLE_IMAGE, method = POST)
    @ResponseBody
    public Map<String, String> imageUpload(@PathVariable String sno,
            MultipartHttpServletRequest req) throws IOException {
        InputStream data = req.getFile("file").getInputStream();
        String grade = req.getParameter("grade");
        return service.imageUpload(data, sno, grade);

    }

    /**
     * @param sno
     * @return
     */
    @PreAuthorize(GET_HAS_ACCESS_TO_STUDENT + API_STUDENT_SINGLE + "\",#sno)")
    @RequestMapping(value = API_STUDENT_SINGLE, method = GET)
    @ResponseBody
    public Student getBySno(@PathVariable String sno) {
        return service.getStudentBySno(sno);
    }

    @PreAuthorize(GET_HAS_ACCESS_TO_STUDENT + API_STUDENT_SINGLE_PROFILE
            + "\",#sno)")
    @RequestMapping(value = API_STUDENT_SINGLE_PROFILE, method = GET)
    @ResponseBody
    public Map<String, Object> getBriefInfoBySno(@PathVariable String sno) {
        // FIXME:应该重写reposotiry的查询方法,只返回需要的字段,这样应该可以提升性能
        return service.getStudentBriefInfo(service.getStudentBySno(sno));
    }

    // 查询学生信息
//    @PreAuthorize(     + API_STUDENT_SEARCH_PROFILE
//            + "\",#condition)")
    @RequestMapping(value = API_STUDENT_SEARCH_PROFILE, method = GET)
    @ResponseBody
    public Map<String, Object> search(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size,
            @RequestParam String condition) {
        // FIXME:应该重写reposotiry的查询方法,只返回需要的字段,这样应该可以提升性能
        return service.conditionSearch(condition, page, size);
    }

    @RequestMapping(value = API_STUDENT_BATCHUPDATE, method = POST)
    @ResponseBody
    public boolean batchUpdate(MultipartHttpServletRequest req) {
        try {
            InputStream data = req.getFile("file").getInputStream();
            return service.batchUpdate(data);
        } catch (IOException e) {
            LOG.error("{}", e);
            return false;
        }
    }

}