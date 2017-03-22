package edu.ecnu.yjsy.web.xj;

import static edu.ecnu.yjsy.constant.Constant.API_XJ;
import static edu.ecnu.yjsy.constant.Constant.PAGE_PAGE;
import static edu.ecnu.yjsy.constant.Constant.PAGE_SIZE;
import static edu.ecnu.yjsy.constant.QueryParameter.PARAM_PAGE;
import static edu.ecnu.yjsy.constant.QueryParameter.PARAM_PAGE_SIZE;
import static edu.ecnu.yjsy.security.authorization.DomainSecurityExpressionRoot.GET_IS_GRANTED_BY_ROLES;
import static edu.ecnu.yjsy.security.authorization.DomainSecurityExpressionRoot.PUT_HAS_DOMAINS_BY_CONDITION;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static edu.ecnu.yjsy.security.authorization.DomainSecurityExpressionRoot.PUT_HAS_ACCESS_TO_STUDENT;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ecnu.yjsy.service.xj.RepoService;
import net.sf.json.JSONObject;

@RestController
public class RepoController {
    private static final String API_REPOSITORY = API_XJ + "/repository";

    private static final String API_REPOSITORY_SINGLE = API_REPOSITORY
            + "/{sno}";

    private static final String API_REPOSITORY_SEARCH_CONDITION = API_REPOSITORY
            + "/search-condition";

    @Autowired
    private RepoService service;

    @SuppressWarnings("unchecked")
    @PreAuthorize(GET_IS_GRANTED_BY_ROLES + API_REPOSITORY_SEARCH_CONDITION
            + "\")")
    @RequestMapping(value = API_REPOSITORY_SEARCH_CONDITION, method = GET)
    @ResponseBody
    public Map<String, Object> searchCondition(@RequestParam String condition,
            @RequestParam(value = PARAM_PAGE,
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = PARAM_PAGE_SIZE,
                    defaultValue = PAGE_SIZE) Integer size) {
        Map<String, Object> sc = (Map<String, Object>) JSONObject
                .toBean(JSONObject.fromObject(condition), Map.class);
        return service.conditionSearch(sc, page, size);
    }

    @PreAuthorize(PUT_HAS_DOMAINS_BY_CONDITION + API_REPOSITORY
            + "\",#condition)")
    @RequestMapping(value = API_REPOSITORY, method = PUT)
    @ResponseBody
    public Map<String, Object> update(@RequestParam String condition) {
        @SuppressWarnings("unchecked")
        Map<String, Object> sc = (Map<String, Object>) JSONObject
                .toBean(JSONObject.fromObject(condition), Map.class);
        return service.transform(sc);
    }

    /**
     *
     * @param sno
     *            学号
     * @return
     */
    @PreAuthorize(PUT_HAS_ACCESS_TO_STUDENT + API_REPOSITORY_SINGLE
            + "\",#sno)")
    @RequestMapping(value = API_REPOSITORY_SINGLE, method = PUT)
    @ResponseBody
    public boolean singleUpdate(@PathVariable String sno) {
        return service.singleUpdate(sno);
    }

}
