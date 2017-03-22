package edu.ecnu.yjsy.web.auth;

import static edu.ecnu.yjsy.constant.Constant.API_AUTH;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import edu.ecnu.yjsy.service.auth.RoleService;

@RestController
// @RequestMapping(API_AUTH + "/roles")
public class RoleController {
    public static final String API_ROLE = API_AUTH + "/roles";

    public static final String API_ROLE_SINGLE = API_ROLE + "/{roleId}";

    public static final String API_ROLE_BATCH_MODIFY = API_ROLE
            + "/batch-modify";

    @Autowired
    private RoleService roleService;

    /**
     * 查询角色列表
     *
     * @return
     */
    @RequestMapping(value = API_ROLE, method = GET)
    @ResponseBody
    public List<Map<String, Object>> getRole() {
        return roleService.getRole();
    }

    /**
     * 创建或更新一个角色
     *
     * @param roleId
     * @param roleName
     * @param domainLevel
     * @param pageIds
     * @return
     */
    @RequestMapping(value = API_ROLE, method = POST)
    @ResponseBody
    public List<Map<String, Object>> save(@RequestParam Long roleId,
                                          @RequestParam String roleName, @RequestParam String domainLevel,
                                          @RequestParam(required = false) long[] pageIds) {
        return roleService.save(roleId, roleName, domainLevel, pageIds);
    }

    /**
     * 删除一个角色
     *
     * @param roleId
     * @return
     */
    @RequestMapping(value = API_ROLE_SINGLE, method = DELETE)
    @ResponseBody
    public List<Map<String, Object>> delete(@PathVariable long roleId) {
        return roleService.delete(roleId);
    }

    /**
     * 查找一个角色
     *
     * @param roleId
     * @return
     */
    @RequestMapping(value = API_ROLE_SINGLE, method = GET)
    @ResponseBody
    public Map<String, Object> findOne(@PathVariable long roleId) {
        return roleService.findOne(roleId);
    }

    @RequestMapping(value = API_ROLE_BATCH_MODIFY, method = POST)
    @ResponseBody
    public Map<String, Object> batchModify(
            MultipartHttpServletRequest request) {
        try {
            return roleService.batchModify(
                    request.getFile("file").getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
