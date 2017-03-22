package edu.ecnu.yjsy.web.notification;

import static edu.ecnu.yjsy.constant.Constant.API_TZ;
import static edu.ecnu.yjsy.constant.Constant.PAGE_PAGE;
import static edu.ecnu.yjsy.constant.Constant.PAGE_SIZE;
import static edu.ecnu.yjsy.constant.QueryParameter.PARAM_PAGE;
import static edu.ecnu.yjsy.constant.QueryParameter.PARAM_PAGE_SIZE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.text.ParseException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ecnu.yjsy.service.notification.MessageService;

@RestController
public class MessageController {
    private static final String API_MESSAGE_PREFIX = API_TZ + "/message";

    private static final String API_MESSAGE_SAVE = API_MESSAGE_PREFIX + "/save";

    private static final String API_MESSAGE_CURRENT_MESSAGES = API_MESSAGE_PREFIX
            + "/current-messages";

    private static final String API_MESSAGE_MESSAGE_DETAILS = API_MESSAGE_PREFIX
            + "/{messageId}/message-details";

    private static final String API_MESSAGE_MESSAGES_BY_ME = API_MESSAGE_PREFIX
            + "/messages-by-me";

    private static final String API_MESSAGE_DELETE = API_MESSAGE_PREFIX
            + "/{messageId}/delete";

    private static final String API_MESSAGE_UPDATE_MESSAGE = API_MESSAGE_PREFIX
            + "/update-message";

    @Autowired
    private MessageService service;

    /**
     * 保存通知
     * 
     * @param message
     *            包含前端传过来的通知详情，有标题、内容、开始时间、通知时间、快速链接
     * @param members
     *            接受该通知的Account.id
     * @param members
     *            接受该通知的MessageGroup.id
     */
    @RequestMapping(value = API_MESSAGE_SAVE, method = POST)
    @ResponseBody
    public void save(@RequestParam String message,
            @RequestParam(required = false) long[] members,
            @RequestParam(required = false) long[] groups) {
        try {
            service.save(message, members, groups);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取到该用户的通知
     * 
     * @param start为通知的开始时间，end为通知的结束时间
     * @return Map<String, Object>
     *         返回一个Map，key是“data”,value包含属于该角色的“通知”，“通知”中包含id、title(标题)、start(开始时间)、end(结束时间)
     *         其格式为{"data":[{"id":id,"title":title,"start":start,"end",end},{...}]}
     */
    @RequestMapping(value = API_MESSAGE_CURRENT_MESSAGES, method = GET)
    @ResponseBody
    public Map<String, Object> getCurrentMessages(
            @RequestParam(defaultValue = "") String start,
            @RequestParam(defaultValue = "") String end) {
        return service.getMessages(start, end);
    }

    /**
     * 根据Message.id来获取通知的详细信息
     * 
     * @param Message.id
     * @return Map<String, Object>
     *         返回一个Map，key是'data',value包含该通知的conent(内容)和link(快速链接,如果没有则不加入)
     *         其格式为{"data":{"conent":conent,"link":link}}
     */
    @RequestMapping(value = API_MESSAGE_MESSAGE_DETAILS, method = GET)
    @ResponseBody
    public Map<String, Object> getMessageDetails(@PathVariable long messageId) {
        return service.getMessageDetails(messageId);
    }

    /**
     * 获取当前用户发送的通知
     * 
     * @param page第几页
     * 
     * @param size每页显示的最大数据条数
     * 
     * @return 返回一个Map{"data",[{"id":id,"title":title,"beginTime":beginTime,"endTime":endTime},{...}],"totalItems":totalItems}
     */
    @RequestMapping(value = API_MESSAGE_MESSAGES_BY_ME, method = GET)
    @ResponseBody
    public Map<String, Object> getMessagesByMe(
            @RequestParam(value = PARAM_PAGE,
                    defaultValue = PAGE_PAGE) int page,
            @RequestParam(value = PARAM_PAGE_SIZE,
                    defaultValue = PAGE_SIZE) int size) {
        return service.getMessagesByMe(page, size);
    }

    /**
     * 根据messageId来删除一则通知,并向前台返回删除后的分页记录
     * 
     * @param messageId
     * 
     * @param page
     * 
     * @param size
     * 
     * @return 返回一个Map{"data",[{"id":id,"title":title,"beginTime":beginTime,"endTime":endTime},{...}],"totalItems":totalItems}
     * 
     */
    @RequestMapping(value = API_MESSAGE_DELETE, method = DELETE)
    @ResponseBody
    public Map<String, Object> delete(@PathVariable long messageId,
            @RequestParam(value = PARAM_PAGE,
                    defaultValue = PAGE_PAGE) int page,
            @RequestParam(value = PARAM_PAGE_SIZE,
                    defaultValue = PAGE_SIZE) int size) {
        return service.delete(messageId, page, size);
    }

    @RequestMapping(value = API_MESSAGE_UPDATE_MESSAGE, method = PUT)
    @ResponseBody
    public void updateMessage(@RequestParam String message) {
        service.updateMessage(message);
    }

}
