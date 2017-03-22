package edu.ecnu.yjsy.service.notification;

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
import edu.ecnu.yjsy.model.message.MessageGroup;
import edu.ecnu.yjsy.model.message.MessageGroupRepository;

@Service
public class MessageGroupService {

    @Autowired
    private MessageGroupRepository messageGroupRepository;

    @Autowired
    private AccountRepository accountRepository;

    /**
     * 获取当前角色所拥有的群
     */
    public Map<String, Object> getCurrentGroups() {
        Account currentUser = accountRepository.findOne(SecurityContextHolder
                .getContext().getAuthentication().getName());
        List<MessageGroup> groups = messageGroupRepository
                .findByOwner(currentUser);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (MessageGroup group : groups) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("id", group.getId());
            temp.put("name", group.getName());
            data.add(temp);
        }
        Map<String, Object> res = new HashMap<>();
        res.put("data", data);
        return res;
    }

    /**
     * 给当前用户添加一个新的群
     * 
     * @param name是群的名称
     * @param selectedMembers是组内成员的Account.id
     * 
     * @return 如果保存成功，则给前台返回该群的名称和id；否则向前台返回‘error’
     */
    public Map<String, Object> saveGroup(String name, long[] selectedMembers) {
        Map<String, Object> res = new HashMap<>();
        MessageGroup messageGroup = getGroup(name, selectedMembers);
        if (messageGroup != null) {
            messageGroup = messageGroupRepository.save(messageGroup);
            Map<String, Object> group = new HashMap<>();
            group.put("id", messageGroup.getId());
            group.put("name", messageGroup.getName());
            res.put("group", group);
            return res;
        }
        res.put("message", "error");
        return res;
    }

    /**
     * 根据MessageGroup.id来获取该群组内的成员信息
     * 
     * @param id是MessageGroup的主键
     * 
     * @return 返回的Map中包含1、selectedMembersId包含了所有成员的Account.id；2、selectedMembersName包含了与selectedMembersId对应的人员姓名
     */
    public Map<String, Object> getGroupMember(long id) {
        MessageGroup group = messageGroupRepository.findOne(id);
        Map<String, Object> res = new HashMap<>();
        List<Long> selectedMembers = new ArrayList<>();
        List<Map<String, Object>> selectedMembersName = new ArrayList<>();
        for (Account member : group.getMembers()) {
            selectedMembers.add(member.getId());
            Map<String, Object> selectedMemberName = new HashMap<>();
            if (member.getStaff() != null)
                selectedMemberName.put("name", member.getStaff().getName());
            else
                selectedMemberName.put("name", member.getStudent().getName());
            selectedMembersName.add(selectedMemberName);
        }
        res.put("selectedMembersId", selectedMembers);
        res.put("selectedMembersName", selectedMembersName);
        return res;
    }

    /**
     * 根据MessageGroup.id来删除该群
     * 
     * @param id是MessageGroup的主键
     * 
     * @return 返回一个message包含success或error
     */
    public Map<String, Object> deleteGroup(long id) {
        Map<String, Object> res = new HashMap<>();
        try {
            messageGroupRepository.delete(id);
        } catch (Exception e) {
            res.put("message", "error");
            return res;
        }
        res.put("message", "success");
        return res;
    }

    /**
     * 给当前用户添加一个新的群
     * 
     * @param id是要修改的MessageGroup.id
     * @param name是群的名称
     * @param selectedMembers是新的组内成员的Account.id
     * 
     * @return 如果保存成功，则给前台返回该群的新的名称和id；否则向前台返回“error”
     */
    public Map<String, Object> modifyGroup(long id, String name,
            long[] selectedMembers) {
        Map<String, Object> res = new HashMap<>();
        if (messageGroupRepository.exists(id)) {
            MessageGroup messageGroup = getGroup(name, selectedMembers);
            messageGroup.setId(id);
            messageGroupRepository.save(messageGroup);
            res.put("message", "success");
            return res;
        }
        res.put("message", "error");
        return res;
    }

    /**
     * 根据群名称和群内成员的Account.id来生成一个MessageGroup
     * name = 
     * */
    private MessageGroup getGroup(String name, long[] selectedMembers) {
        if (name != null && !name.toString().equals("")
                && selectedMembers.length > 1) {
            Account owner = accountRepository.findOne(SecurityContextHolder
                    .getContext().getAuthentication().getName());
            MessageGroup messageGroup = new MessageGroup();
            messageGroup.setName(name);
            Set<Account> members = new HashSet<>();
            for (long selectedItem : selectedMembers) {
                Account member = new Account();
                member.setId(selectedItem);
                members.add(member);
            }
            messageGroup.setMembers(members);
            messageGroup.setOwner(owner);
            return messageGroup;
        }
        return null;
    }

}
