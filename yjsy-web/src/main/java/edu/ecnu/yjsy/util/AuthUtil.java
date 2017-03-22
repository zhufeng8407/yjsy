package edu.ecnu.yjsy.util;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ecnu.yjsy.model.auth.Account;
import edu.ecnu.yjsy.model.auth.AccountPrivilege;
import edu.ecnu.yjsy.model.auth.AccountPrivilegeRepository;
import edu.ecnu.yjsy.model.auth.AccountRepository;
import edu.ecnu.yjsy.model.auth.Role;
import edu.ecnu.yjsy.model.auth.RoleRepository;
import edu.ecnu.yjsy.model.staff.Staff;
import edu.ecnu.yjsy.model.staff.StaffRepository;
import edu.ecnu.yjsy.model.student.UnitRepository;

/**
 * 用于为系统用户初始化相应的角色权限。
 */
@Service
public class AuthUtil {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private StaffRepository staffRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private AccountPrivilegeRepository domainRepo;

    @Autowired
    private UnitRepository unitRepo;

    /**
     * 创建用于演示的超级管理员帐号
     */
    public void createAdmin() {
        Staff staff = new Staff();
        staff.setName("超级管理员");
        staff.setSno("001");
        staff = staffRepo.save(staff);

        Account account = new Account();
        account.setStaff(staff);
        account.setUsername("admin");
        account.setPassword("admin");

        // 设置帐号的角色
        Set<Role> roles = new HashSet<Role>();

        // 设置角色能够访问的页面
        Role role = roleRepo.findByName("超级管理员");
        roles.add(role);
        Role schoolRole = roleRepo.findByName("研究生秘书（一级教学单位)");
        roles.add(schoolRole);
        account.setRoles(roles);
        account = accountRepo.save(account);

        // 设置帐号的权限
        AccountPrivilege accountPrivilege = new AccountPrivilege();
        accountPrivilege.setAccount(account);
        accountPrivilege.setRole(role);
        domainRepo.save(accountPrivilege);

        accountPrivilege = new AccountPrivilege();
        accountPrivilege.setAccount(account);
        accountPrivilege.setRole(schoolRole);
        accountPrivilege.setSchoolCode(unitRepo.findOne(3l).getSchoolCode());
        domainRepo.save(accountPrivilege);

        accountPrivilege = new AccountPrivilege();
        accountPrivilege.setAccount(account);
        accountPrivilege.setRole(schoolRole);
        accountPrivilege.setSchoolCode(unitRepo.findOne(21l).getSchoolCode());
        domainRepo.save(accountPrivilege);
    }

}
