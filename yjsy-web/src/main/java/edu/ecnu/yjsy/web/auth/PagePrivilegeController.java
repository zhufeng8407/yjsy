package edu.ecnu.yjsy.web.auth;

import static edu.ecnu.yjsy.constant.Constant.API_AUTH;
import static edu.ecnu.yjsy.security.authorization.DomainSecurityExpressionRoot.GET_IS_GRANTED_BY_ROLES;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ecnu.yjsy.service.auth.PageService;

@RestController
// @RequestMapping(API_AUTH + "/menus")
public class PagePrivilegeController {
    public static final String API_MENUS = API_AUTH + "/menus";

    public static final String API_GRANTED_PAGES = API_MENUS + "/granted-pages";

    @Autowired
    private PageService service;

    /**
     * 返回当前用户活跃角色下可访问的菜单列表
     *
     * @return
     */
    @PreAuthorize(GET_IS_GRANTED_BY_ROLES + API_GRANTED_PAGES + "\")")
    @RequestMapping(value = API_GRANTED_PAGES, method = GET)
    @ResponseBody
    public List<Map<String, Object>> getMenu() {
        return service.getMenu();
    }

    /**
     * 返回所有的页面列表
     *
     * @return
     */
    @PreAuthorize(GET_IS_GRANTED_BY_ROLES + API_MENUS + "\")")
    @RequestMapping(value = API_MENUS, method = GET)
    @ResponseBody
    public List<Map<String, Object>> getPages() {
        return service.getPages();
    }

    @PreAuthorize(GET_IS_GRANTED_BY_ROLES + API_MENUS + "\")")
    @RequestMapping(value = API_MENUS, method = GET, params = { "keywords" })
    @ResponseBody
    public Map<String, Object> searchPage(@RequestParam String keywords) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", service.getPage(keywords));
        return map;
    }

}
