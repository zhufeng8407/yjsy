package edu.ecnu.yjsy.service.xj;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ecnu.yjsy.model.staff.Staff;
import edu.ecnu.yjsy.model.staff.StaffRepository;
import edu.ecnu.yjsy.model.student.Student;
import edu.ecnu.yjsy.model.student.StudentRepository;
import edu.ecnu.yjsy.security.authorization.DomainBasedAuthorizer;
import edu.ecnu.yjsy.service.BaseService;
import edu.ecnu.yjsy.service.search.SearchSQLService;

@Service
public class SupervisorService extends BaseService {

    private static final String[] FILE_FIELDS = {"学号", "导师"};

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private StaffRepository staffRepo;

    @Autowired
    DomainBasedAuthorizer authorizer;

    @Override
    public void initConditionSearch(SearchSQLService searchSQLService) {
        // DO NOTHING
    }

    public Map<String, Object> upload(InputStream is, String uploadType, String api, String method)
            throws InvalidFormatException, IOException {
        DomainBasedAuthorizer.AuthorizationContext ctx = authorizer.getDomainsGrantedByAPI(api, method);

        try {
            Workbook workbook = createWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);

            int errorNum = 0;
            Map<String, Object> map = new HashMap<>();

            // 检查文件格式
            Row titles = sheet.getRow(0);
            if (!checkFormat(titles, FILE_FIELDS)) {
                map.put("state", 0);
                return map;
            }

            Map<String, Integer> schema = parseSchema(titles);

            Row row = null;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);
                List<String> lists = parseRow(row, FILE_FIELDS, schema);
                String sno = lists.get(0);

                if (authorizer.isAccessGrantedByDomain(ctx, sno)) {
                    Student student =
                            sno != null ? studentRepo.findBySno(sno) : null;

                    if (student != null) {
                        if (student.getSupervisor() != null) {
                            errorNum++;
                            handleErrorData(row, FILE_FIELDS, uploadType,
                                    "已分配导师，请走异动流程！");
                        } else {
                            String staffSno = lists.get(1);

                            Staff staff = staffSno != null ?
                                    staffRepo.findBySno(staffSno) :
                                    null;
                            if (staff != null) {
                                student.setSupervisor(staff);
                                studentRepo.save(student);
                            } else {
                                errorNum++;
                                handleErrorData(row, FILE_FIELDS, uploadType, "");
                            }
                        }
                    } else {
                        errorNum++;
                        handleErrorData(row, FILE_FIELDS, uploadType, String.format("学号%s对应的学生不存在", sno));
                    }
                } else {
                    errorNum++;
                    handleErrorData(row, FILE_FIELDS, uploadType, String.format("无权限操作学号%s对应的学生", sno));
                }
            }

            map.put("state", 1);
            map.put("totalcount", sheet.getLastRowNum());
            map.put("errorcount", errorNum);
            return map;
        } finally {
            close(is);
        }
    }

}
