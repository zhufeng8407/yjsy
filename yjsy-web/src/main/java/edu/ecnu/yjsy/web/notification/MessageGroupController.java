package edu.ecnu.yjsy.web.notification;

import static edu.ecnu.yjsy.constant.Constant.API_TZ;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ecnu.yjsy.service.notification.MessageGroupService;

@RestController
public class MessageGroupController {
    private static final String API_GROUP_PREFIX = API_TZ + "/group";

    private static final String API_GROUP_CURRENT_GROUPS = API_GROUP_PREFIX
            + "/current-groups";

    private static final String API_GROUP_SAVE = API_GROUP_PREFIX + "/save";

    private static final String API_GROUP_MEMBERS = API_GROUP_PREFIX
            + "/{groupId}/members";

    private static final String API_GROUP_DELETE = API_GROUP_PREFIX
            + "/{groupId}/delete";

    private static final String API_GROUP_MODIFY = API_GROUP_PREFIX
            + "/{groupId}/modify";

    @Autowired
    private MessageGroupService service;

    @RequestMapping(value = API_GROUP_CURRENT_GROUPS, method = GET)
    @ResponseBody
    public Map<String, Object> getCurrentGroups() {
        return service.getCurrentGroups();
    }

    @RequestMapping(value = API_GROUP_SAVE, method = POST)
    @ResponseBody
    public Map<String, Object> saveGroup(@RequestParam String name,
            @RequestParam long[] selectedMembers) {
        return service.saveGroup(name, selectedMembers);
    }

    @RequestMapping(value = API_GROUP_MEMBERS, method = GET)
    @ResponseBody
    public Map<String, Object> getGroupMember(
            @PathVariable(value = "groupId") long id) {
        return service.getGroupMember(id);
    }

    @RequestMapping(value = API_GROUP_DELETE, method = DELETE)
    @ResponseBody
    public Map<String, Object> deleteGroup(
            @PathVariable(value = "groupId") long id) {
        return service.deleteGroup(id);
    }

    @RequestMapping(value = API_GROUP_MODIFY, method = PUT)
    @ResponseBody
    public Map<String, Object> modifyGroup(
            @PathVariable(value = "groupId") long id, @RequestParam String name,
            @RequestParam long[] selectedMembers) {
        return service.modifyGroup(id, name, selectedMembers);
    }
}
