package edu.ecnu.yjsy.service;

import edu.ecnu.yjsy.constant.QueryParameter;
import edu.ecnu.yjsy.model.staff.Staff;
import edu.ecnu.yjsy.model.staff.StaffRepository;
import edu.ecnu.yjsy.model.student.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StaffService {

    @Autowired
    private StaffRepository repo;
    
    public List<Staff> getAllStaff(){
        return repo.findAll();
    }

    public Map<String, Object> getStaff(String supervisor) {
        Map<String, Object> res = new HashMap<>();
        List<Map<String, Object>> data = new ArrayList<>();
        //FIXME: 模糊匹配？
        supervisor = "%" + supervisor + "%";
        for (Staff item : repo.findIdAndName(supervisor)) {
            Map<String, Object> tmp = new HashMap<>();
            tmp.put(QueryParameter.ID, item.getId());
            tmp.put(QueryParameter.STAFF_NAME, item.getName());
            data.add(tmp);
        }
        res.put("data", data);
        return res;
    }

    public Map<String, Object> getStaffBriefInfo(String staffsno) {
        Map<String, Object> res = new HashMap<String, Object>();
        Staff staff = repo.findBySno(staffsno);
        res.put(QueryParameter.ID, staff.getId());
        res.put(QueryParameter.STAFF_NO, staffsno);
        res.put(QueryParameter.STAFF_NAME, staff.getName());
        res.put(QueryParameter.PROFESSION, staff.getProfession());
        Set<Unit> units = staff.getUnits();
        if (units != null) {
            List<String> schools = new ArrayList<>();
            List<String> departments = new ArrayList<>();
            for (Unit unit : units) {
                schools.add(unit.getSchool());
                departments.add(unit.getDepartment());
            }
            res.put(QueryParameter.SCHOOLS, schools);
            res.put(QueryParameter.DEPARTMENTS, departments);
        }
        res.put(QueryParameter.IS_MASTER_SUPERVISOR, staff.isSuperviseMaster() ? "是" : "否");
        res.put(QueryParameter.IS_DOCTOR_SUPERVISOR, staff.isSuperviseDoctor() ? "是" : "否");
        return res;
    }
}
