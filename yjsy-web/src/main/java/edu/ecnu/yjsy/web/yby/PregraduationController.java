package edu.ecnu.yjsy.web.yby;

import static edu.ecnu.yjsy.constant.Constant.API_YBY;
import static edu.ecnu.yjsy.constant.Constant.PAGE_PAGE;
import static edu.ecnu.yjsy.constant.Constant.PAGE_SIZE;
import static edu.ecnu.yjsy.constant.QueryParameter.PARAM_PAGE;
import static edu.ecnu.yjsy.constant.QueryParameter.PARAM_PAGE_SIZE;
import static edu.ecnu.yjsy.security.authorization.DomainSecurityExpressionRoot.GET_HAS_ACCESS_TO_STUDENT;
import static edu.ecnu.yjsy.security.authorization.DomainSecurityExpressionRoot.GET_IS_GRANTED_BY_ROLES;
import static edu.ecnu.yjsy.security.authorization.DomainSecurityExpressionRoot.POST_HAS_ACCESS_TO_STUDENT;
import static edu.ecnu.yjsy.security.authorization.DomainSecurityExpressionRoot.POST_IS_GRANTED_BY_ROLES;
import static edu.ecnu.yjsy.security.authorization.DomainSecurityExpressionRoot.PUT_IS_GRANTED_BY_ROLES;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ecnu.yjsy.model.student.PregraduationStatus;
import edu.ecnu.yjsy.service.yby.PregraduationService;
import net.sf.json.JSONObject;

/**
 * @author PYH
 */
@RestController
public class PregraduationController {

    private static final String API_YBY_SUBMIT = API_YBY + "/{sno}" + "/submit";

    private static final String API_YBY_SUBMIT_CONFIG = API_YBY
            + "/submit-config";

    private static final String API_YBY_GET_AUDITDATA = API_YBY
            + "/get-auditdata";

    private static final String API_YBY_PASS_AUDIT = API_YBY + "/pass-audit";

    private static final String API_YBY_GET_CURRENT_STUDENT = API_YBY + "/{sno}"
            + "/current";

    private static final String API_YBY_ITIALIZE = API_YBY + "/itialize";

    private static final String API_YBY_CONFIRM = API_YBY + "/{sno}"
            + "/confirm";

    private static final String API_YBY_RETURN = API_YBY + "/{sno}" + "/return";

    private static final String API_YBY_SEARCH_CONDITION = API_YBY
            + "/search-condition";

    private static final String API_YBY_CONFIG_INFO = API_YBY + "/config-info";

    @Autowired
    private PregraduationService service;

    @PreAuthorize(GET_IS_GRANTED_BY_ROLES + API_YBY_CONFIG_INFO + "\")")
    @RequestMapping(value = API_YBY_CONFIG_INFO, method = GET)
    @ResponseBody
    public Map<String, Object> getConfigInfo()throws Exception {
        return service.getConfigInfo();
    }
    // =================================================================
    // 管理员用的函数
    // =================================================================

    /**
     * 管理员按条件查看学生预毕业审核状态
     * 
     * @param conditions
     * @param page
     * @param size
     * @return
     */
    @SuppressWarnings("unchecked")
    @PreAuthorize(GET_IS_GRANTED_BY_ROLES + API_YBY_SEARCH_CONDITION + "\")")
    @RequestMapping(value = API_YBY_SEARCH_CONDITION, method = GET)
    @ResponseBody
    public Map<String, Object> searchCondition(@RequestParam String conditions,
            @RequestParam(value = PARAM_PAGE,
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = PARAM_PAGE_SIZE,
                    defaultValue = PAGE_SIZE) Integer size) {
        Map<String, Object> sc = (Map<String, Object>) JSONObject
                .toBean(JSONObject.fromObject(conditions), Map.class);
        return service.conditionSearch(sc, page, size);
    }

    /**
     * 管理员将学生预毕业审核状态由<code>通过</code>改为<code>可修改</code>
     * 
     * @param sno
     * @return
     * @throws Exception
     */
    @PreAuthorize(POST_HAS_ACCESS_TO_STUDENT + API_YBY_RETURN + "\",#sno)")
    @RequestMapping(value = API_YBY_RETURN, method = POST)
    @ResponseBody
    public boolean applyReturn(@PathVariable String sno) throws Exception {
        return service.modifyPregraduationStatus(sno, PregraduationStatus.可修改);
    }

    /**
     * 管理员设置允许研究生可以进行审核个人预毕业信息的开放时间
     * 
     * @param day
     *            提前的天数研究生可以审核个人预毕业信息
     * @return
     */
    @PreAuthorize(POST_IS_GRANTED_BY_ROLES + API_YBY_ITIALIZE + "\")")
    @RequestMapping(value = API_YBY_ITIALIZE, method = POST)
    @ResponseBody
    public boolean intialize(@RequestParam String day) throws Exception{
        service.intialize(Integer.parseInt(day));
        return true;
    }

    /**
     * 管理员配置预毕业学籍字段的修改状态
     * 
     * @param apply
     * @return
     */
    @PreAuthorize(PUT_IS_GRANTED_BY_ROLES + API_YBY_SUBMIT_CONFIG + "\")")
    @RequestMapping(value = API_YBY_SUBMIT_CONFIG, method = PUT)
    @ResponseBody
    public boolean submitConfig(@RequestParam String apply) {
        service.submitConfig(apply);
        return true;
    }

    /**
     * 管理员查看预毕业的申请信息
     * 
     * @param page
     * @param size
     * @return
     */
    @PreAuthorize(GET_IS_GRANTED_BY_ROLES + API_YBY_GET_AUDITDATA + "\")")
    @RequestMapping(value = API_YBY_GET_AUDITDATA, method = GET)
    @ResponseBody
    public List<Map<String, Object>> getAuditdata(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {
        return service.getAuditdata(page, size);

    }

    /**
     * 管理员审核研究生提交的预毕业申请：<code>通过</code>或者<code>否决</code>
     * 
     * @param id
     * @param sno
     * @param accountId
     * @param flag
     * @return
     * @throws Exception
     */
    @PreAuthorize(PUT_IS_GRANTED_BY_ROLES + API_YBY_PASS_AUDIT + "\")")
    @RequestMapping(value = API_YBY_PASS_AUDIT, method = PUT)
    @ResponseBody
    public List<Map<String, Object>> passAudit(@RequestParam String id,
            @RequestParam String sno, @RequestParam String accountId,
            @RequestParam boolean flag) throws Exception {
        return service.isPassAudit(Long.parseLong(id), sno,
                Long.parseLong(accountId), flag);
    }

    // =================================================================
    // 学生用的函数
    // =================================================================

    /**
     * 学生确认预毕业信息无误
     * 
     * @param sno
     * @return
     * @throws Exception
     */
    @PreAuthorize(POST_HAS_ACCESS_TO_STUDENT + API_YBY_CONFIRM + "\",#sno)")
    @RequestMapping(value = API_YBY_CONFIRM, method = POST)
    @ResponseBody
    public Map<String, Object> confirm(@PathVariable String sno)
            throws Exception {
        if (service.modifyPregraduationStatus(sno,
                PregraduationStatus.通过)) { return service.getCurStudent(sno); }
        return null;
    }

    /**
     * 获取当前学生的预毕业信息
     * 
     * @param sno
     * @return
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    @PreAuthorize(GET_HAS_ACCESS_TO_STUDENT + API_YBY_GET_CURRENT_STUDENT
            + "\",#sno)")
    @RequestMapping(value = API_YBY_GET_CURRENT_STUDENT, method = GET)
    @ResponseBody
    public Map<String, Object> getCurStudent(@PathVariable String sno)
            throws Exception {
        return service.getCurStudent(sno);
    }

    /**
     * 学生提交申请修改的预毕业信息
     * 
     * @param sno
     * @param apply
     * @return
     * @throws Exception
     */
    @PreAuthorize(POST_HAS_ACCESS_TO_STUDENT + API_YBY_SUBMIT + "\",#sno)")
    @RequestMapping(value = API_YBY_SUBMIT, method = POST)
    @ResponseBody
    public boolean submitApplication(@PathVariable String sno,
            @RequestParam String apply) throws Exception {
        return service.submitApplication(sno, apply);
    }

}
