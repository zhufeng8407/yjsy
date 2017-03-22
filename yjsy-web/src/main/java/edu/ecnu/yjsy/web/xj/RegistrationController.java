package edu.ecnu.yjsy.web.xj;

import edu.ecnu.yjsy.service.xj.CheckinService;
import edu.ecnu.yjsy.service.xj.FeeService;
import edu.ecnu.yjsy.service.xj.RegistrationService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static edu.ecnu.yjsy.constant.Constant.*;
import static edu.ecnu.yjsy.constant.QueryParameter.PARAM_ITEMS;
import static edu.ecnu.yjsy.security.authorization.DomainSecurityExpressionRoot.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author liyanbin
 * @author xulinhao
 */

@RestController
public class RegistrationController {

    private static final String API_REGISTRATION_PREFIX = API_XJ
            + "/registration";

    private static final String API_REGISTRATION_RESET = API_REGISTRATION_PREFIX
            + "/reset";

    private static final String API_REGISTRATION_UPLOAD_REGISTER = API_REGISTRATION_PREFIX
            + "/upload";

    private static final String API_REGISTRATION_STUDENTS = API_REGISTRATION_PREFIX
            + "/students";

    private static final String API_REGISTRATION_EXPORT = API_REGISTRATION_PREFIX
            + "/export";

    private static final String API_REGISTRATION_EXPORT_ERROR = API_REGISTRATION_PREFIX
            + "/error";

    private static final String API_REGISTRATION_STATS = API_REGISTRATION_PREFIX
            + "/stats";

    private static final String API_REGISTRATION_FEE = API_REGISTRATION_PREFIX
            + "/fee";

    private static final Logger LOG = LoggerFactory
            .getLogger(RegistrationController.class);

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private CheckinService checkinService;

    @Autowired
    private FeeService feeService;

    // 查询学生信息
    // @PreAuthorize(GET_HAS_DOMAINS_BY_CONDITION + API_REGISTRATION_STUDENTS
    // + "\",#condition)")
    @RequestMapping(value = API_REGISTRATION_STUDENTS, method = GET)
    @ResponseBody
    public Map<String, Object> search(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size,
            @RequestParam String condition) {
        return registrationService.conditionSearch(condition, page, size);
    }

    @RequestMapping(value = API_REGISTRATION_FEE, method = GET)
    @ResponseBody
    public Map<String, Object> searchFee(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size,
            @RequestParam String condition) {
        return feeService.conditionSearch(condition, page, size);
    }

    // FIXME: 所有手动注册都是依赖于流水表有记录的情况，所以在使用本功能前一定要先对本学年本学期进行重置，流水表中保证
    // 对每个在籍的学生都存在一条记录，否则流水表没有记录的情况下点击注册会存在问题
    @PreAuthorize(POST_IS_GRANTED_BY_ROLES + API_REGISTRATION_PREFIX + "\")")
    @RequestMapping(value = API_REGISTRATION_PREFIX, method = POST)
    @ResponseBody
    public boolean register(@RequestParam long[] studentIds) {
        for (Long id : studentIds) {
            registrationService.register(id);
        }
        return true;
    }

    @PreAuthorize(PUT_IS_GRANTED_BY_ROLES + API_REGISTRATION_RESET + "\")")
    @RequestMapping(value = API_REGISTRATION_RESET, method = PUT)
    @ResponseBody
    public boolean reset() {
        return registrationService.reset();
    }

    @PreAuthorize(POST_IS_GRANTED_BY_ROLES + API_REGISTRATION_UPLOAD_REGISTER
            + "\")")
    @RequestMapping(value = API_REGISTRATION_UPLOAD_REGISTER, method = POST)
    @ResponseBody
    public Boolean upload(MultipartHttpServletRequest req) {
        try {
            InputStream data = req.getFile("file").getInputStream();
            String type = req.getParameter("type");

            if (type.equals("checkin")) {
                return checkinService.upload(data, type, true);
            } else if (type.equals(
                    "fee")) { return feeService.upload(data, type, true); }
        } catch (Exception e) {
            LOG.error("{}", e);
        }
        return null;
    }

    // FIXME: 前端搜索都传递相关部门的编码，学号或者工号
    @PreAuthorize(GET_HAS_DOMAINS_BY_CONDITION + API_REGISTRATION_EXPORT
            + "\",#condition)")
    @RequestMapping(value = API_REGISTRATION_EXPORT, method = GET)
    @ResponseBody
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public byte[] export(@RequestParam String condition) throws IOException {
        Map<String, Object> sc = (Map<String, Object>) JSONObject
                .toBean(JSONObject.fromObject(condition), Map.class);
        List<Map> students = (List<Map>) registrationService
                .conditionSearch(sc, 0, Integer.MAX_VALUE).get(PARAM_ITEMS);

        return registrationService.export(students);
    }

    // FIXME: 导入时将不规范的信息导出到新的EXCEL表中
    // @PreAuthorize(GET_HAS_DOMAINS_BY_CONDITION +
    // API_REGISTRATION_EXPORT_ERROR
    // + "\",#condition)")
    @RequestMapping(value = API_REGISTRATION_EXPORT_ERROR, method = GET)
    @ResponseBody
    public byte[] exportError(@RequestParam String tag,
            HttpServletRequest request) throws Exception {
        return feeService.exportError(tag);

    }

    // FIXME: 前端搜索都传递相关部门的编码，学号或者工号
    // @PreAuthorize(GET_HAS_DOMAINS_BY_CONDITION + API_REGISTRATION_STATS
    // + "\",#condition)")
    @RequestMapping(value = API_REGISTRATION_STATS, method = GET)
    @ResponseBody
    public Map<String, Object> stats(@RequestParam String condition) {
        return registrationService.stats(condition);
    }

}
