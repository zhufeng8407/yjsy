package edu.ecnu.yjsy.service.search.mapper;

import edu.ecnu.yjsy.model.constant.Column;
import edu.ecnu.yjsy.model.staff.StaffRepository;
import edu.ecnu.yjsy.model.constant.Table;
import edu.ecnu.yjsy.model.student.StudentRepository;
import edu.ecnu.yjsy.constant.QueryParameter;
import edu.ecnu.yjsy.model.change.AuditStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * 根据<code>StatusChangeAuditProcessor</code>，本类需要抽取审批步骤号，审批角色，审批人;
 *
 * @author xiafan
 */
public class StatusChangeAuditMapper extends CommonRowMapper {
    private StaffRepository staffRepository;
    private StudentRepository studentRepository;

    public StatusChangeAuditMapper(CommonRowMapper parent) {
        super(parent);
        // 审批的信息
        longFields.put("auditId", column(Table.STATUS_CHANGE_AUDIT, Column.ID));
        stringFields.put("reason",
                column(Table.STATUS_CHANGE_REQUEST, Column.REASON));
        stringFields.put("status",
                column(Table.STATUS_CHANGE_AUDIT, Column.AUDIT_STATUS));
        timestampFields.put("auditDate",
                column(Table.STATUS_CHANGE_AUDIT, Column.AUDIT_AT));
        timestampFields.put("requestDate",
                column(Table.STATUS_CHANGE_REQUEST, Column.CREATED_AT));
        // 审批步骤信息
        shortFields.put("sequence", column(Table.AUDIT_WORKFLOW, "sequence"));
        stringFields.put(QueryParameter.ROLE_NAME, column(Table.ROLE, "name"));
        // 异动类型
        stringFields.put("major1",
                column(Table.STATUS_CHANGE_TYPE, Column.MAJOR));
        stringFields.put("minor1",
                column(Table.STATUS_CHANGE_TYPE, Column.MINOR));
        // 学号
        longFields.put("sno1", column(Table.STUDENT, Column.SNO));

        // 以下两个为审批人的信息
        longFields.put("auditorStudent", column("auditor", Column.STUDENT));
        longFields.put("auditorStaff", column("auditor", Column.STAFF));

        // 代申请人真实姓名
        stringFields.put("applicantName",
                column(Table.STATUS_CHANGE_REQUEST, Column.APPLICANT_NAME));
    }

    public void setRepository(StaffRepository staffRepository,
            StudentRepository studentRepository) {
        this.staffRepository = staffRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        @SuppressWarnings("unchecked")
        Map<String, Object> ret = (Map<String, Object>) super.mapRow(rs,
                rowNum);
        AuditStatus[] auditStatus = AuditStatus.values();

        // 通过repository获取审批人
        if (ret.get("auditorStudent") != null) {
            ret.put("account", studentRepository
                    .findSnoById((Long) ret.get("auditorStudent")));
        } else if (ret.get("auditorStaff") != null) {
            ret.put("account", staffRepository
                    .findSnoById((Long) ret.get("auditorStaff")));
        }

        int status = Integer.parseInt(ret.get("status").toString());
        ret.put("auditStatus", auditStatus[status]);
        switch (status) {
        case 0:
            ret.put("icon", "glyphicon-ok");
            ret.put("class", "success");
            break;

        case 1:
            ret.put("icon", "glyphicon-remove");
            ret.put("class", "danger");
            break;

        case 2:
            ret.put("icon", "glyphicon-edit");
            ret.put("class", "info");
            break;

        case 3:
            ret.put("icon", "glyphicon-time");
            ret.put("class", "warning");
            break;
        }
        return ret;
    }
}
