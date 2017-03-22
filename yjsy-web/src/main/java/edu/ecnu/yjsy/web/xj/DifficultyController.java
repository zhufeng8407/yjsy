package edu.ecnu.yjsy.web.xj;

import static edu.ecnu.yjsy.constant.Constant.API_XJ;
import static edu.ecnu.yjsy.constant.Constant.PAGE_PAGE;
import static edu.ecnu.yjsy.constant.Constant.PAGE_SIZE;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import edu.ecnu.yjsy.service.xj.DifficultyService;
import net.sf.json.JSONObject;

@RestController
public class DifficultyController {

    public static final String API_DIFFICULTY = API_XJ + "/difficulties";

    public static final String API_DIFFICULTY_ITEM = API_DIFFICULTY
            + "/{diffId}";

    public static final String API_DIFFICULTY_GET_YEAR = API_DIFFICULTY
            + "/diff-year";

    private static final String API_DIFFICULTY_EXPORT_ERROR = API_DIFFICULTY
            + "/error";

    @Autowired
    private DifficultyService service;

    // 拿到助困表中存在的学年
    @PreAuthorize(GET_IS_GRANTED_BY_ROLES + API_DIFFICULTY_GET_YEAR + "\")")
    @RequestMapping(value = API_DIFFICULTY_GET_YEAR, method = GET)
    @ResponseBody
    public List<String> getDiffYear() {
        return service.getDiffYear();
    }

    // 按条件查询
    @SuppressWarnings("unchecked")
    @PreAuthorize(GET_IS_GRANTED_BY_ROLES + API_DIFFICULTY + "\")")
    @RequestMapping(value = API_DIFFICULTY, method = GET,
            params = { "condition", PARAM_PAGE, PARAM_PAGE_SIZE })
    @ResponseBody
    public Map<String, Object> conditionSearch(@RequestParam String condition,
            @RequestParam(value = PARAM_PAGE,
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = PARAM_PAGE_SIZE,
                    defaultValue = PAGE_SIZE) Integer size) {
        Map<String, Object> sc = (Map<String, Object>) JSONObject
                .toBean(JSONObject.fromObject(condition), Map.class);
        return service.conditionSearch(sc, page, size);
    }

    // 批量上传
    @PreAuthorize(POST_IS_GRANTED_BY_ROLES + API_DIFFICULTY + "\")")
    @RequestMapping(value = API_DIFFICULTY, method = POST)
    @ResponseBody
    public Map<String, Object> upload(MultipartHttpServletRequest req) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result = service.upload(req.getFile("file").getInputStream(),
                    req.getParameter("type"));
        } catch (Exception e) {
            result.put("state", 0);
            return result;
        }

        return result;
    }

    // FIXME:这种删除方式极其危险,如果不小心重复提交,就会误删数据,合理的做法还是前端把要删除的记录IDs传回来
    @PreAuthorize(DELETE_IS_GRANTED_BY_ROLES + API_DIFFICULTY_ITEM + "\")")
    @RequestMapping(value = API_DIFFICULTY_ITEM, method = DELETE)
    @ResponseBody
    public boolean delete(@PathVariable long diffId) {
        service.delete(diffId);
        return true;
    }

    @PreAuthorize(PUT_IS_GRANTED_BY_ROLES + API_DIFFICULTY + "\")")
    @RequestMapping(value = API_DIFFICULTY, method = PUT)
    @ResponseBody
    public boolean update(@RequestParam(defaultValue = "0") long diffId,
            @RequestParam String year, @RequestParam String diffLevel) {
        return service.update(diffId, year, diffLevel);
    }

    // FIXME: 导入时将不规范的信息导出到新的EXCEL表中
    @RequestMapping(value = API_DIFFICULTY_EXPORT_ERROR, method = GET)
    @ResponseBody
    public byte[] exportError(@RequestParam String tag,
            HttpServletRequest request) throws Exception {
        return service.exportError(tag);
    }

}
