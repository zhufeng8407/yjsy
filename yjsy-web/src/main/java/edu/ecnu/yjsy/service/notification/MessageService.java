package edu.ecnu.yjsy.service.notification;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import edu.ecnu.yjsy.model.auth.Account;
import edu.ecnu.yjsy.model.auth.AccountRepository;
import edu.ecnu.yjsy.model.message.Message;
import edu.ecnu.yjsy.model.message.MessageGroup;
import edu.ecnu.yjsy.model.message.MessageGroupRepository;
import edu.ecnu.yjsy.model.message.MessageRepository;
import net.sf.json.JSONObject;

@Service
public class MessageService {

    @Autowired
    private MessageGroupRepository messageGroupRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MessageRepository messageRepository;

    /**
     * 保存通知
     * 
     * @param msg
     *            包含前端传过来的通知详情，有标题、内容、开始时间、通知时间、快速链接
     * @param members
     *            接受该通知的Account.id
     * @param members
     *            接受该通知的MessageGroup.id
     */
    @SuppressWarnings("unchecked")
    public void save(String msg, long[] members, long[] groups)
            throws ParseException {
        Map<String, Object> obj = (Map<String, Object>) JSONObject
                .toBean(JSONObject.fromObject(msg), Map.class);
        Message message = new Message();
        if (obj.get("title") != null && !obj.get("title").toString().equals(""))
            message.setTitle(obj.get("title").toString());
        if (obj.get("content") != null
                && !obj.get("content").toString().equals(""))
            message.setContent(obj.get("content").toString());
        if (obj.get("link") != null && !obj.get("link").toString().equals(""))
            message.setLink(obj.get("link").toString());
        if (obj.get("startsAt") != null
                && !obj.get("startsAt").toString().equals(""))
            message.setBeginTime(
                    internationToBeiJin(obj.get("startsAt").toString()));
        if (obj.get("endsAt") != null
                && !obj.get("endsAt").toString().equals(""))
            message.setEndTime(
                    internationToBeiJin(obj.get("endsAt").toString()));
        Set<Account> recivers = new HashSet<>();
        MessageGroup tmp;
        if (groups != null) for (long groupid : groups) {
            tmp = messageGroupRepository.findOne(groupid);
            recivers.addAll(tmp.getMembers());
        }
        if (members != null) for (long memberid : members) {
            recivers.add(accountRepository.findOne(memberid));
        }
        message.setReceivers(recivers);
        Account author = accountRepository.findOne(SecurityContextHolder
                .getContext().getAuthentication().getName());
        message.setAuthor(author);
        message = messageRepository.save(message);
    }

    /**
     * 获取到该用户的通知
     * 
     * @return Map<String, Object>
     *         返回一个Map，key是'data',value包含属于该角色的通知，通知主要包含id、title(标题)、start(开始时间)、end(结束时间)
     *         其格式为{"data":[{"id":id,"title":title,"start":start,"end",end},{...}]}
     */
    public Map<String, Object> getMessages(String start, String end) {
        Account currentAccount = accountRepository.findOne(SecurityContextHolder
                .getContext().getAuthentication().getName());
        List<Message> messages = messageRepository.findByAccountStartEnd(
                currentAccount.getId(), start.split("\"")[1],
                end.split("\"")[1]);
        Map<String, Object> res = new HashMap<>();
        List<Map<String, Object>> data = new ArrayList<>();

        for (Message message : messages) {
            Map<String, Object> tmp = new HashMap<>();
            tmp.put("id", message.getId());
            tmp.put("title", message.getTitle());
            tmp.put("start", message.getBeginTime());
            if (message.getEndTime() != null)
                tmp.put("end", message.getEndTime());
            data.add(tmp);
        }

        res.put("data", data);
        return res;
    }

    /**
     * 根据通知的id来获取通知的详细信息
     * 
     * @param Message.id
     * @return Map<String, Object>
     *         返回一个Map，key是'data',value包含该通知的conent(内容)和link(快速链接,如果没有则不加入)
     *         其格式为{"data":{"conent":conent,"link":link}}
     */
    public Map<String, Object> getMessageDetails(long id) {
        Message message = messageRepository.findOne(id);
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        data.put("content", message.getContent());
        if (message.getLink() != null) data.put("link", message.getLink());
        res.put("data", data);

        return res;
    }

    /**
     * 获取当前用户发送的通知
     * 
     * @param page
     *            第几页
     * 
     * @param size
     *            每页显示的最大数据条数
     * 
     * @return 返回一个Map{"data",[{"id":id,"title":title,"beginTime":beginTime,"endTime":endTime},{...}],"totalItems":totalItems}
     */
    public Map<String, Object> getMessagesByMe(int page, int size) {
        Account currentAccount = accountRepository.findOne(SecurityContextHolder
                .getContext().getAuthentication().getName());
        Map<String, Object> res = new HashMap<>();
        List<Message> messages = messageRepository
                .findByAuthor(currentAccount.getId(), page * size, size);
        List<Map<String, Object>> simpleMessages = new ArrayList<>();
        for (Message message : messages) {
            Map<String, Object> tmp = new HashMap<>();
            tmp.put("id", message.getId());
            tmp.put("title", message.getTitle());
            tmp.put("startsAt", message.getBeginTime());
            tmp.put("endsAt", message.getEndTime());
            simpleMessages.add(tmp);
        }
        res.put("data", simpleMessages);
        res.put("totalItems", messageRepository.count());
        return res;
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
    public Map<String, Object> delete(long messageId, int page, int size) {
        messageRepository.delete(messageId);
        return getMessagesByMe(page, size);
    }

    private String internationToBeiJin(String time) {
        if (time == null) return null;
        String[] t = time.split("T");
        int hh = (Integer.valueOf(t[1].substring(0, 2)) + 8) % 24;
        if (hh / 10 == 0)
            return t[0] + "T0" + t[1].replaceFirst(t[1].substring(0, 2),
                    String.valueOf(hh));
        else
            return t[0] + "T" + t[1].replaceFirst(t[1].substring(0, 2),
                    String.valueOf(hh));
    }

    @SuppressWarnings("unchecked")
    public void updateMessage(String msg) {
        Map<String, Object> obj = (Map<String, Object>) JSONObject
                .toBean(JSONObject.fromObject(msg), Map.class);
        Message message = messageRepository
                .findOne(Long.valueOf(obj.get("id").toString()));
        if (obj.get("title") != null && !obj.get("title").toString().equals(""))
            message.setTitle(obj.get("title").toString());
        if (obj.get("content") != null
                && !obj.get("content").toString().equals(""))
            message.setContent(obj.get("content").toString());
        if (obj.get("link") != null && !obj.get("link").toString().equals(""))
            message.setLink(obj.get("link").toString());
        if (obj.get("startsAt") != null
                && !obj.get("startsAt").toString().equals(""))
            message.setBeginTime(
                    internationToBeiJin(obj.get("startsAt").toString()));
        if (obj.get("endsAt") != null
                && !obj.get("endsAt").toString().equals(""))
            message.setEndTime(
                    internationToBeiJin(obj.get("endsAt").toString()));
        messageRepository.save(message);
    }

}
