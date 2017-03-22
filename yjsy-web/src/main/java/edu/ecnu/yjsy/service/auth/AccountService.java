package edu.ecnu.yjsy.service.auth;

import static edu.ecnu.yjsy.constant.QueryParameter.DEPARTMENT_CODE;
import static edu.ecnu.yjsy.constant.QueryParameter.DEPARTMENT_NAME;
import static edu.ecnu.yjsy.constant.QueryParameter.DOMAINS;
import static edu.ecnu.yjsy.constant.QueryParameter.DOMAIN_LEVEL;
import static edu.ecnu.yjsy.constant.QueryParameter.ROLE_ID;
import static edu.ecnu.yjsy.constant.QueryParameter.ROLE_NAME;
import static edu.ecnu.yjsy.constant.QueryParameter.SCHOOL_CODE;
import static edu.ecnu.yjsy.constant.QueryParameter.SCHOOL_NAME;
import static edu.ecnu.yjsy.constant.QueryParameter.STUDENT_NO;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.ecnu.yjsy.model.auth.Account;
import edu.ecnu.yjsy.model.auth.AccountPrivilege;
import edu.ecnu.yjsy.model.auth.AccountPrivilegeRepository;
import edu.ecnu.yjsy.model.auth.AccountRepository;
import edu.ecnu.yjsy.model.auth.DomainLevel;
import edu.ecnu.yjsy.model.auth.Role;
import edu.ecnu.yjsy.model.auth.RoleRepository;
import edu.ecnu.yjsy.model.staff.Staff;
import edu.ecnu.yjsy.model.staff.StaffRepository;
import edu.ecnu.yjsy.model.student.Student;
import edu.ecnu.yjsy.model.student.StudentRepository;
import edu.ecnu.yjsy.model.student.UnitRepository;
import edu.ecnu.yjsy.security.authentication.GrantedDomainAuthority;
import edu.ecnu.yjsy.service.BaseService;
import edu.ecnu.yjsy.service.search.SearchSQLExecutor;
import edu.ecnu.yjsy.service.search.SearchSQLService;
import edu.ecnu.yjsy.service.search.processor.AccountPrivilegeProcessor;
import edu.ecnu.yjsy.service.search.processor.AccountProcessor;
import edu.ecnu.yjsy.service.search.processor.AccountStudentProcessor;
import edu.ecnu.yjsy.service.search.processor.StaffProcessor;
import net.sf.json.JSONObject;

/**
 * @author guhang
 * @author xiafan
 */
@Service
public class AccountService extends BaseService {

    private static final String[] FILE_FIELD = {"职工号", "角色名", "权限范围"};

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private AccountPrivilegeRepository accountPrivilegeRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private AccountPrivilegeRepository domainRepo;

    @Autowired
    private UnitRepository unitRepo;

    @Autowired
    private StaffRepository staffRepo;

    @Autowired
    private StudentRepository studentRepository;

    private SearchSQLExecutor staffExec;

    private SearchSQLExecutor studentExec;

    @Autowired
    @Override
    public void initConditionSearch(SearchSQLService searchSQLService) {
        // 还需要把账号相关的权限数据查询出来
        // 通过职工搜索账号
        staffExec = searchSQLService.build()
                .addProcessor(new AccountProcessor())
                .addProcessor(new StaffProcessor())
                .addProcessor(new AccountPrivilegeProcessor()).create();

        // 通过学生搜索账号
        studentExec = searchSQLService.build()
                .addProcessor(new AccountProcessor())
                .addProcessor(new AccountStudentProcessor()).create();
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> query(String searchCondition) {
        Map<String, Object> sc = (Map<String, Object>) JSONObject
                .toBean(JSONObject.fromObject(searchCondition), Map.class);
        if (sc.get(STUDENT_NO) != null) {
            return studentExec.execute(sc);
        } else {
            return staffExec.execute(sc);
        }
    }

    /**
     * 返回帐号已经获得的角色，同时也将帐号未获得的角色返回
     *
     * @param accountId
     * @return
     */
    public Map<String, Object> findGrantedRoles(String accountId) {
        Map<String, Object> res = new HashMap<>();
        Account account = accountRepo
                .findOneWithStaffAndStudentByusername(accountId);
        if (account == null) {
            res.put("error", "查无此人！");
            return res;
        }
        res.put("accountId", account.getId());
        Staff staff = account.getStaff();
        Student student = account.getStudent();
        if (staff != null) {
            res.put("name", staff.getName());
            res.put("type", "老师");
        }
        if (student != null) {
            res.put("name", student.getName());
            res.put(DEPARTMENT_NAME, student.getUnit().getDepartment());
            res.put("type", "学生");
        }
        Set<Role> roles = account.getRoles();
        List<Role> allRoles = roleRepo.findAll();
        List<Map<String, Object>> accountRoles = new ArrayList<>();
        List<Map<String, Object>> otherRoles = new ArrayList<>();
        for (Role role : roles) {
            allRoles.remove(role);
            accountRoles.add(getAccountRole(role,
                    domainRepo.findByAccountAndRole(account, role)));
        }
        res.put("accountRoles", accountRoles);
        for (Role role : allRoles) {
            Map<String, Object> map = new HashMap<>();
            map.put(ROLE_NAME, role.getName());
            map.put(DOMAIN_LEVEL, role.getDomainLevel().toString());
            map.put(ROLE_ID, role.getId());
            otherRoles.add(map);
        }
        res.put("otherRoles", otherRoles);
        return res;
    }

    /**
     * @param accountId 帐号ID
     * @param roleId    角色ID
     */
    public void deleteRole(long accountId, long roleId) {
        Account account = accountRepo.findOne(accountId);
        Role role = roleRepo.findOne(roleId);
        account.getRoles().remove(role);
        accountRepo.save(account);
        domainRepo.deleteByAccountAndRole(account, role);
    }

    /**
     * 为用户添加角色
     *
     * @param account
     * @param role
     * @param privilege
     * @return
     */
    public Map<String, Object> addRole(Account account, Role role,
                                       List<AccountPrivilege> privilege) {
        Set<Role> roles = account.getRoles();
        if (roles.contains(role)) {
            // role已经存在，先清空role已有的权限
            domainRepo.deleteByAccountAndRole(account, role);
        } else {
            roles.add(role);
            // 更新帐号的角色
            roles.add(role);
            account.setRoles(roles);
            accountRepo.save(account);
        }
        domainRepo.save(privilege);

        Map<String, Object> res = new HashMap<>();
        List<Map<String, Object>> accountRoles = new ArrayList<>();
        roles.forEach(r -> accountRoles.add(getAccountRole(r,
                domainRepo.findAllByAccountAndRole(account, r))));
        res.put("accountRoles", accountRoles);
        return res;
    }

    /**
     * 获取帐号在某个角色下的访问域
     *
     * @param accountId
     * @param roleId
     * @return
     */
    public List<Map<String, Object>> getDomains(long accountId, long roleId) {
        Account account = new Account();
        account.setId(accountId);
        Role role = new Role();
        role.setId(roleId);
        List<AccountPrivilege> accountPrivileges = domainRepo
                .findAllByAccountAndRole(account, role);
        List<Map<String, Object>> res = new ArrayList<>();
        for (AccountPrivilege accountPrivilege : accountPrivileges) {
            Map<String, Object> map = new HashMap<>();
            if (!accountPrivilege.isUniversity()
                    && !(!String.valueOf(accountPrivilege.getSupervisor())
                    .equals("0")
                    || !String.valueOf(accountPrivilege.getStudent())
                    .equals("0"))) {//如果不是校级、导师、学生，则认为是院系一级或二级
                if (accountPrivilege.getSchoolCode() != null) {
                    map.put(SCHOOL_CODE, accountPrivilege.getSchoolCode());
                    map.put(SCHOOL_NAME, unitRepo
                            .getSchoolName(accountPrivilege.getSchoolCode()));
                }
                if (accountPrivilege.getDepartmentCode() != null) {
                    map.put(DEPARTMENT_CODE,
                            accountPrivilege.getDepartmentCode());
                    map.put(DEPARTMENT_NAME, unitRepo.getDepartmentName(
                            accountPrivilege.getDepartmentCode()));
                } else {
                    map.put(DEPARTMENT_CODE, "所有");
                    map.put(DEPARTMENT_NAME, "所有");
                }
            } else if (!String.valueOf(accountPrivilege.getSupervisor())
                    .equals(null)) {
                Staff staff = staffRepo.findOne(accountPrivilege.getSupervisor());
                if (staff != null) {
                    map.put("name", staff.getName());
                    map.put("id", staff.getId());
                    // map.put("unit", student.getUnit().getDepartment());
                    // map.put("grade", student.getGrade());
                }
            } else if (!String.valueOf(accountPrivilege.getStudent())
                    .equals(null)) {
                // TODO:学生需要怎么处理?
            }
            res.add(map);
        }
        return res;
    }

    /**
     * 为帐号添加(或者更新)角色以及角色下对应的访问域
     *
     * @param accountId
     * @param roleId
     * @param domains
     * @return
     */
    public Map<String, Object> updateRole(long accountId, Long roleId,
                                          String[] domains) {
        Account account = accountRepo.findOne(accountId);
        Role role = roleRepo.findOne(roleId);
        domainRepo.deleteByAccountAndRole(account, role);
        List<AccountPrivilege> accountPrivilegeList = stringToDomains(account,
                role, domains);
        return addRole(account, role, accountPrivilegeList);
    }

    /**
     * 返回角色具体能够访问的域列表（院系，部所等）
     *
     * @param role
     * @param roleAccountPrivileges
     * @return
     */
    private Map<String, Object> getAccountRole(Role role,
                                               List<AccountPrivilege> roleAccountPrivileges) {
        Map<String, Object> map = RoleService.toMapRepr(role, false);

        DomainLevel domainLevel = role.getDomainLevel();
        if (!domainLevel.equals(DomainLevel.校级)) {
            StringBuilder domains = new StringBuilder();
            if (roleAccountPrivileges != null) {
                for (AccountPrivilege accountPrivilege : roleAccountPrivileges) {
                    if (domainLevel.equals(DomainLevel.一级))
                        domains.append(unitRepo.getSchoolName(
                                accountPrivilege.getSchoolCode())).append("|")
                                .append(accountPrivilege.getSchoolCode());
                    else if (domainLevel.equals(DomainLevel.二级)) {
                        if (accountPrivilege.getDepartmentCode() != null)
                            domains.append(unitRepo.getDepartmentName(
                                    accountPrivilege.getDepartmentCode()))
                                    .append("|").append(accountPrivilege
                                    .getDepartmentCode());
                        else if (accountPrivilege.getSchoolCode() != null)
                            domains.append(unitRepo.getSchoolName(
                                    accountPrivilege.getSchoolCode()))
                                    .append("->所有");
                    } else if (domainLevel.equals(DomainLevel.导师)) {
                        domains.append(accountRepo
                                .findOne(accountPrivilege
                                                .getSupervisor())
                                .getStaff().getName());
                    }

                    domains.append(",");
                }
            }

            String domainStr = (domains.length() == 0) ? "无"
                    : domains.substring(0, domains.length() - 1);
            map.put(DOMAINS, domainStr);
        }
        return map;
    }

    /**
     * 基于前端传回的domain字符形式，创建相应的domain对象
     *
     * @param account
     * @param role
     * @param domains
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<AccountPrivilege> stringToDomains(Account account, Role role,
                                                   String[] domains) {
        List<AccountPrivilege> accountPrivilegeList = new ArrayList<>();
        if (domains != null && domains.length > 0) {
            for (int i = 0; i < domains.length; i++) {
                AccountPrivilege accountPrivilege = new AccountPrivilege();
                accountPrivilege.setAccount(account);
                accountPrivilege.setRole(role);

                Map<String, Object> domainObj;
                try {
                    domainObj = (Map<String, Object>) JSONObject.toBean(
                            JSONObject.fromObject(domains[i]), HashMap.class);
                } catch (Exception e) {
                    // FIXME: 为什么会出现这种情况
                    domainObj = (Map<String, Object>) JSONObject.toBean(
                            JSONObject
                                    .fromObject(StringUtils.join(domains, ",")),
                            HashMap.class);
                    i = domains.length;
                }

                switch (role.getDomainLevel()) {
                    case 导师:
                        accountPrivilege.setSupervisor(
                                Long.valueOf(domainObj.get("id").toString()));
                        accountPrivilegeList.add(accountPrivilege);
                        break;
                    default:
                        accountPrivilege.setSchoolCode(
                                domainObj.get(SCHOOL_CODE).toString());
                        if (!domainObj.get(DEPARTMENT_CODE).toString()
                                .equals("所有")) {
                            accountPrivilege.setDepartmentCode(
                                    domainObj.get(DEPARTMENT_CODE).toString());
                        }
                        accountPrivilegeList.add(accountPrivilege);
                }

            }
        }
        return accountPrivilegeList;
    }

    /**
     * 设置当前用户的活跃角色
     *
     * @param accountId
     * @param roleIds
     */
    public void setActiveRoles(long accountId, long[] roleIds) {
        List<GrantedDomainAuthority> authorities = new ArrayList<>();
        Account account = new Account();
        account.setId(accountId);
        Role role = new Role();
        for (long roleId : roleIds) {
            role.setId(roleId);
            domainRepo.findAllByAccountAndRole(account, role)
                    .forEach(domain -> authorities
                            .add(new GrantedDomainAuthority(domain)));

            // FIXME: 为什么这样做？,使用了entitygraph不是应该要能够外键都见在了吗？
            /*
             * for(AccountPrivilege domain :
             * domainRepo.findAllByAccountAndRole(account, role)){
             * GrantedDomainAuthority tmp =new GrantedDomainAuthority(domain);
             * tmp.getAccountPrivilege().getRole().getPages();
             * authorities.add(tmp); }
             */
        }
        // 更新security context中的用户使用的domains
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        authentication.getName(),
                        authentication.getCredentials(), authorities));
    }

    /**
     * 查找当前用户能够管辖的Staff账号 FIXME 目前是查询所有Staff账号，而不是根据用户所管辖的Staff
     *
     * @return 返回一个对象，包含Account.id,Staff.sno,Staff,name
     */
    public Map<String, Object> getStaffAccount() {
        List<Account> accounts = accountRepo.findAllwithIdStaff();
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> res = new HashMap<>();
        for (Account account : accounts) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("id", account.getId());
            temp.put("sno", account.getStaff().getSno());
            temp.put("name", account.getStaff().getName());
            data.add(temp);
        }
        res.put("data", data);
        return res;
    }

    /**
     * 查找当前用户能够管辖的Student账号 FIXME 目前是查询所有Student账号，而不是根据用户所管辖的Student
     *
     * @return 返回一个对象，包含Account.id,Student.sno,Student,name
     */
    public Map<String, Object> getStudentAccount() {
        List<Account> accounts = accountRepo.findAllwithIdStudent();
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> res = new HashMap<>();
        for (Account account : accounts) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("id", account.getId());
            temp.put("sno", account.getStudent().getSno());
            temp.put("name", account.getStudent().getName());
            data.add(temp);
        }
        res.put("data", data);
        return res;
    }

    /**
     * 为学生创建帐号，分配角色，并且设置权限
     *
     * @param student
     */
    public void createStudentAccount(Student student) {
        Account account = accountRepo.findByStudent(student);
        if (account != null) {
            return;
        }

        // 创建帐号，设置帐号的角色
        account = new Account();
        account.setStudent(student);
        account.setUsername(student.getSno());
        account.setPassword(student.getSno());

        Set<Role> roles = new HashSet<Role>();
        Role role = roleRepo.findByName(Role.NEWBIE);
        roles.add(role);
        account.setRoles(roles);

        account = accountRepo.save(account);

        // 初始化帐号的权限
        AccountPrivilege accountPrivilege = new AccountPrivilege();
        accountPrivilege.setAccount(account);
        accountPrivilege.setRole(role);
        accountPrivilege.setStudent(student.getId());
        domainRepo.save(accountPrivilege);
    }

    /**
     * 为职工创建账号
     *
     * @param staff
     */
    public void createStaffAccount(Staff staff) {
        Account account = accountRepo.findByStaff(staff);
        if (account != null) {
            return;
        }

        account = new Account();
        account.setStaff(staff);
        account.setUsername(staff.getSno());
        account.setPassword(staff.getSno());
        accountRepo.save(account);

        // FIXME: 默认导师的角色?
        updateStaffPrivilege(staff.getSno(), Role.SUPERVISOR,
                Arrays.asList(staff.getSno()));
    }

    /**
     * 上传职工的权限数据
     *
     * @param data
     */
    @SuppressWarnings("unchecked")
    public void uploadStaffPrivilege(byte[] data) {
        DefaultLineMapper<String> lineMapper = new DefaultLineMapper<String>();
        lineMapper.setFieldSetMapper(fields -> {
            List<String> codes = Arrays
                    .asList(fields.readString("权限范围").split("_"));
            return updateStaffPrivilege(fields.readString("职工号"),
                    fields.readString("角色名"), codes);
        });

        FlatFileItemReader<String> itemReader = createReader(data, lineMapper);

        String result = null;
        do {
            try {
                result = itemReader.read();
                if (result != null && !result.equals("ok")) {
                    // FIXME: 17-2-22 we can record errors as feedback to users
                }
            } catch (Exception e) {
                // FIXME: 17-2-22 we can record errors as feedback to users
                e.printStackTrace();
            }
        }
        while (result != null);
    }

    /**
     * 为教职工创建帐号，分配角色，并且设置权限
     *
     * @param staffNo
     * @param roleName
     * @param codes:   需要管理的院系的编码或者导师，学生的工号
     */
    public String updateStaffPrivilege(String staffNo, String roleName,
                                       List<String> codes) {
        String ret = "ok";

        Staff staff = staffRepo.findBySno(staffNo);
        Role role = roleRepo.findByName(roleName);

        // 创建帐号，设置帐号的角色
        Account account = accountRepo.findByStaff(staff);
        if (account == null) {
            ret = "账号不存在";
        } else if (account.getRoles().contains(role)) {
            // FIXEME: error may be caused for duplicated privileges
            ret = "用户已经拥有角色:" + roleName;
        } else {
            account.getRoles().add(role);
            accountRepo.save(account);

            // 初始化帐号的权限
            for (String code : codes) {
                domainRepo.save(createAccountPrivilege(account, role, code));
            }
        }

        return ret;
    }

    /**
     * FIXME: 对于学生和导师，之后都使用sno和staff no来保存
     *
     * @param role
     * @param code
     * @return
     */
    public AccountPrivilege createAccountPrivilege(Account account, Role role,
                                                   String code) {
        AccountPrivilege accountPrivilege = new AccountPrivilege();
        accountPrivilege.setAccount(account);
        accountPrivilege.setRole(role);

        switch (role.getDomainLevel()) {
            case 校级:
                accountPrivilege.setUniversity(true);
                break;
            case 一级:
                accountPrivilege.setSchoolCode(code);
                break;
            case 二级:
                accountPrivilege
                        .setSchoolCode(code.substring(0, code.length() - 2));
                accountPrivilege.setDepartmentCode(code);
                break;
            case 学生:
                accountPrivilege.setStudent(studentRepository.findIdBySno(code));
                break;
            default:
                accountPrivilege.setSupervisor(staffRepo.findIdBySno(code));
        }
        return accountPrivilege;
    }

    /**
     * 处理上传的文件，更新用户在角色下的具体访问权限
     *
     * @param data
     * @return
     * @throws InvalidFormatException
     * @throws IOException
     */
    @Transactional
    public Map<String, Object> uploadPrivilege(InputStream data)
            throws InvalidFormatException, IOException {
        int errorNumber = 0;
        Map<String, Object> result = new HashMap<>();
        try {
            Workbook workbook = createWorkbook(data);
            Sheet sheet = workbook.getSheetAt(0);
            if (!checkFormat(sheet.getRow(0), FILE_FIELD)) {
                result.put("state", 1);
            } else {
                Row r = sheet.getRow(0);
                Map<String, Integer> schema = new HashMap<>();
                for (int i = r.getFirstCellNum(); i < r.getLastCellNum(); i++) {
                    schema.put(r.getCell(i).getStringCellValue(), i);
                }
                List<AccountPrivilege> accountPrivileges = new ArrayList<>();
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    r = sheet.getRow(i);
                    String username = getCellValue(r,
                            schema.get(FILE_FIELD[0]));
                    String rolename = getCellValue(r,
                            schema.get(FILE_FIELD[1]));
                    String rawcodes = getCellValue(r,
                            schema.get(FILE_FIELD[2]));
                    if (username == null || rolename == null) {
                        errorNumber++;
                        handleErrorData(r, FILE_FIELD, "account", "");
                        continue;
                    }
                    Account account = accountRepo.findOne(username);
                    Role role = roleRepo.findByName(rolename);
                    String[] codes = {};
                    if (rawcodes != null)
                        codes = getCellValue(r, schema.get(FILE_FIELD[2]))
                                .split("_");
                    if (account == null || role == null) {
                        errorNumber++;
                        handleErrorData(r, FILE_FIELD, "account", "");
                        continue;
                    }
                    AccountPrivilege accountPrivilege = new AccountPrivilege();
                    accountPrivilege.setAccount(account);
                    accountPrivilege.setRole(role);
                    for (String code : codes) {
                        if (code.length() == 4)
                            accountPrivilege.setSchoolCode(code);
                        else if (code.length() == 6)
                            accountPrivilege.setDepartmentCode(code);
                    }
                    if (codes.length == 0) {
                        accountPrivilege.setUniversity(true);
                    }
                    accountPrivileges.add(accountPrivilege);
                    stateStore.setState(PROGRESS_FIELD,
                            i * 90 / sheet.getLastRowNum());
                }
                accountPrivilegeRepo.save(accountPrivileges);
                result.put("total", sheet.getLastRowNum());
                result.put("error", errorNumber);
                result.put("key", "account");
                result.put("state", 2);
                stateStore.setState(PROGRESS_FIELD, 100);
            }
        } finally {
            close(data);
        }
        return result;
    }

    /**
     * 通过用户名，来查找对应职工的信息。
     *
     * @param username 帐号名
     **/
    public Map<String, Object> findStaffInfoByUsername(String username) {
        Account account = accountRepo
                .findOneWithStaffAndStudentByusername(username);
        Map<String, Object> res = new HashMap<>();
        if (account == null || account.getStaff() == null)
            return res;
        res.put("name", account.getStaff().getName());
        res.put("id", account.getStaff().getId());
        return res;
    }

}
