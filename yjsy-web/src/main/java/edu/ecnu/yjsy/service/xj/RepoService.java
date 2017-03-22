package edu.ecnu.yjsy.service.xj;

import static edu.ecnu.yjsy.constant.QueryParameter.PARAM_ITEMS;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import edu.ecnu.yjsy.model.auth.Account;
import edu.ecnu.yjsy.model.auth.AccountPrivilege;
import edu.ecnu.yjsy.model.auth.AccountPrivilegeRepository;
import edu.ecnu.yjsy.model.auth.AccountRepository;
import edu.ecnu.yjsy.model.auth.Role;
import edu.ecnu.yjsy.model.auth.RoleRepository;
import edu.ecnu.yjsy.model.change.StatusChangeRequest;
import edu.ecnu.yjsy.model.student.Student;
import edu.ecnu.yjsy.model.student.StudentRepository;
import edu.ecnu.yjsy.service.BaseService;
import edu.ecnu.yjsy.service.auth.AccountService;
import edu.ecnu.yjsy.service.search.SearchSQLService;
import edu.ecnu.yjsy.service.search.processor.CountProcessor;

/**
 * 新生转库 新生转库主要是2个部分：
 * <p>
 * 1. 在批量导入前： isNew = true 并且 学籍的学籍异动字段的学籍变动情况含有“保留入学资格
 * ”，将该生的报道状态置为false；注册状态也置为false。
 * <p>
 * 2.三个月左右新生转库时： isNew = true 并且 注册状态=true 的新生isNew置为false。
 *
 * @author PYH
 */

@Service
public class RepoService extends BaseService {

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private AccountPrivilegeRepository domainRepo;

    private Role newBie;
    private Role oldStudent;

    @Autowired
    public void initConditionSearch(SearchSQLService searchSQLService) {
        exec = searchSQLService.defaultSearch().create();
        countExec = searchSQLService.defaultSearch()
                .addProcessor(new CountProcessor()).create();
    }

    @Autowired
    public void initStudentRole() {
        newBie = roleRepository.findByName(Role.NEWBIE);
        oldStudent = roleRepository.findByName(Role.STUDENT);
    }

    /**
     * 在批量导入前，先处理上一届的未入学新生 FIXME: 这个函数貌似啥也没有干！！！
     *
     * @return
     */
    public Map<String, Object> updatePastNewStudent() {
        Map<String, Object> map = new HashMap<>();
        List<Student> students = studentRepo.findByIsNew(true);
        for (Student student : students) {
            for (StatusChangeRequest statusChangeRequest : student
                    .getRequests()) {
                if (statusChangeRequest.getType().getMajor().equals("保留入学资格")) {
                    // FIXME: 之后这两个字段会从学籍中被删除掉,这是要为保留学籍的学生添加
                    // student.setCheckin(false);
                    // student.setRegister(false);
                    break;
                }
            }
        }
        return map;
    }

    public void updateStudentPrivilege(Student student) {
        Account account = accountRepository.findByStudent(student);
        accountService.deleteRole(account.getId(), newBie.getId());
        // 初始化帐号的权限
        AccountPrivilege accountPrivilege = new AccountPrivilege();
        accountPrivilege.setAccount(account);
        accountPrivilege.setRole(oldStudent);
        accountPrivilege.setStudent(student.getId());
        domainRepo.save(accountPrivilege);
    }

    /**
     * 新生按条件批量转库，转库失败记录，写入文件
     *
     * @param sc
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> transform(Map<String, Object> sc) {
        String res = "转库已完成，失败记录请查看文件RepositoryFail.xlsx";
        ClassPathResource resource = new ClassPathResource(
                "/src/main/resources/result");
        File file = new File(resource.getPath());

        XSSFWorkbook book = new XSSFWorkbook();
        XSSFSheet sheet = book.createSheet("RepositoryFail");

        XSSFRow head = sheet.createRow(0);
        head.createCell(0).setCellValue("序号");
        head.createCell(1).setCellValue("学号");
        head.createCell(2).setCellValue("姓名");
        head.createCell(3).setCellValue("学院");
        head.createCell(4).setCellValue("专业");
        head.createCell(5).setCellValue("转库失败原因");
        int num = 1;

        // 便遍历学生
        for (Map<String, Object> map : (List<Map<String, Object>>) conditionSearch(
                sc, 0, Integer.MAX_VALUE).get(PARAM_ITEMS)) {
            Student student = studentRepo.findBySno(map.get("sno").toString());

            String reason = "";
            if (student != null) {
                if (student.isNew() == true) {
                    // 设置权限
                    updateStudentPrivilege(student);

                    student.setNew(false);
                    studentRepo.save(student);
                    continue;
                } else {
                    reason = "非新生";
                }

                // 能走到这，说明转库失败，写入文件
                XSSFRow row = sheet.createRow(num);
                row.createCell(0).setCellValue(num);
                row.createCell(1).setCellValue(student.getSno());
                row.createCell(2).setCellValue(student.getName());
                row.createCell(3).setCellValue(student.getUnit().getSchool());
                row.createCell(4)
                        .setCellValue(student.getUnit().getDepartment());
                row.createCell(5).setCellValue(reason);
                num++;
            }
        }

        FileOutputStream os;
        try {
            // FIXME: 如果多个人并发执行的话,是不是会出错?
            os = new FileOutputStream(file + "/RepositoryFail.xlsx");
            book.write(os);
            os.close();
        } catch (Exception e) {}

        Map<String, Object> map = new HashMap<>();
        map.put(PARAM_ITEMS, res);
        return map;
    }

    /**
     * 单个新生转库
     *
     * @param sno
     * @return
     */
    public boolean singleUpdate(String sno) {
        Student student = studentRepo.findBySno(sno);
        // 该生是新生，并且该生已注册
        if (student != null && student.isNew()) {
            // FIXME
            Account account = accountRepository.findByStudent(student);
            // 设置权限
            updateStudentPrivilege(student);

            student.setNew(false);
            studentRepo.save(student);
            return true;
        } else {
            return false;
        }
    }

}
