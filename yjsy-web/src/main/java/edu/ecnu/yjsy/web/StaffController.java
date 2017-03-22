package edu.ecnu.yjsy.web;

import static edu.ecnu.yjsy.constant.Constant.API_STAFF;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ecnu.yjsy.model.staff.Staff;
import edu.ecnu.yjsy.service.StaffService;

@RestController
public class StaffController {
    public static final String API_STAFF_ALL = API_STAFF + "/all-staff";
    public static final String API_STAFF_SINGLE = API_STAFF + "/{staffsno}";

    public static final String API_STAFF_SINGLE_PROFILE = API_STAFF_SINGLE + "/profile";

    @Autowired
    private StaffService service;

    @RequestMapping(value = API_STAFF_ALL, method = GET)
    @ResponseBody
    public List<Staff> getAllStaff() {
        return service.getAllStaff();
    }

    @RequestMapping(value = API_STAFF, method = GET, params = {"supervisor"})
    @ResponseBody
    public Map<String, Object> staff(@RequestParam String supervisor) {
        return service.getStaff(supervisor);
    }

    @RequestMapping(value = API_STAFF_SINGLE_PROFILE, method = GET)
    @ResponseBody
    public Map<String, Object> getBriefInfoBySno(@PathVariable String staffsno) {
        return service.getStaffBriefInfo(staffsno);
    }

}
