package edu.ecnu.yjsy.service.auth;

import edu.ecnu.yjsy.model.auth.AccountPrivilege;
import edu.ecnu.yjsy.model.auth.AccountPrivilegeRepository;
import edu.ecnu.yjsy.model.auth.AccountRepository;
import edu.ecnu.yjsy.model.auth.RoleRepository;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static edu.ecnu.yjsy.constant.QueryParameter.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiafan
 * @author guhang
 */
@Service
public class AccountPrivilegeService {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private AccountPrivilegeRepository accountPrivilegeRepo;

    public void save(ArrayList<Map<String, Object>> privileges) {
        //先清除account相关的所有权限
        accountPrivilegeRepo.deleteByAccountId(
                Long.valueOf(privileges.get(0).get("account").toString()));

        for (Map<String, Object> privilegeString : privileges) {

            AccountPrivilege accountPrivilege = new AccountPrivilege();

            if (privilegeString.get(IS_UNIVERSITY) != null && privilegeString
                    .get(IS_UNIVERSITY).toString().equals("是"))
                accountPrivilege.setUniversity(true);
            else
                accountPrivilege.setUniversity(false);
            if (privilegeString.get(SCHOOL_CODE) != null)
                accountPrivilege.setSchoolCode(
                        privilegeString.get(SCHOOL_CODE).toString());
            if (privilegeString.get(DEPARTMENT_CODE) != null)
                accountPrivilege.setDepartmentCode(
                        privilegeString.get(DEPARTMENT_CODE).toString());
            if (privilegeString.get(SUPERVISOR_ID) == null || privilegeString
                    .get(SUPERVISOR_ID).toString().equals("") || privilegeString
                    .get(SUPERVISOR_ID).toString().equals("null"))
                accountPrivilege.setSupervisor(-1);
            else {
                accountPrivilege.setSupervisor(Long.parseLong(
                        privilegeString.get(SUPERVISOR_ID).toString()));
            }
            if (privilegeString.get(STUDENT_ID) == null || privilegeString
                    .get(STUDENT_ID).toString().equals("") || privilegeString
                    .get(STUDENT_ID).toString().equals("null"))
                accountPrivilege.setStudent(-1);
            else {
                accountPrivilege.setStudent(Long.parseLong(
                        privilegeString.get(STUDENT_ID).toString()));
            }

            accountPrivilege.setAccount(accountRepo.findOne(Long.parseLong(
                    (privilegeString.get(ACCOUNT_ID).toString()))));
            accountPrivilege.setRole(roleRepo.findByName(
                    privilegeString.get("role").toString()));

            accountPrivilegeRepo.save(accountPrivilege);
        }
    }


    @SuppressWarnings("unchecked")
    public void save(String privilege) {

        ArrayList<Map<String, Object>> privileges = new ArrayList<Map<String, Object>>();
        Pattern pattern = Pattern.compile("\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(privilege);
        while (matcher.find()) {
            Map<String, Object> sc = (Map<String, Object>) JSONObject
                    .toBean(JSONObject.fromObject(matcher.group()), Map.class);
            privileges.add(sc);
        }

        save(privileges);
    }

}
