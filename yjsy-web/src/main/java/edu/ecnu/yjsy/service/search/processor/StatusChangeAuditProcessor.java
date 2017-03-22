package edu.ecnu.yjsy.service.search.processor;

import edu.ecnu.yjsy.model.constant.Column;
import edu.ecnu.yjsy.model.constant.Table;
import edu.ecnu.yjsy.model.staff.StaffRepository;
import edu.ecnu.yjsy.model.student.StudentRepository;
import edu.ecnu.yjsy.service.search.SearchContext;
import edu.ecnu.yjsy.service.search.mapper.StatusChangeAuditMapper;

import java.util.Map;

import static edu.ecnu.yjsy.model.constant.Table.STATUS_CHANGE_AUDIT;
import static edu.ecnu.yjsy.model.constant.Table.UNIT;

/**
 * 处理学籍异动的搜索条件
 */
public class StatusChangeAuditProcessor extends ISearchProcessor {
    private StaffRepository staffRepository;
    private StudentRepository studentRepository;

    public StatusChangeAuditProcessor(StaffRepository staffRepository,
            StudentRepository studentRepository) {
        this.staffRepository = staffRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public void process(Map<String, Object> searchConditions,
            SearchContext context) {

        if (searchConditions.get(Column.ACCOUNT) != null
                && !"".equals(
                        searchConditions.get(Column.ACCOUNT).toString().trim())
                && !"null".equals(searchConditions.get(Column.ACCOUNT)
                        .toString().trim())) {
            context.numericEqual(STATUS_CHANGE_AUDIT, Column.ACCOUNT,
                    searchConditions.get(Column.ACCOUNT).toString());
        }
        if (searchConditions.get(Column.AUDIT_STATUS) != null
                && !"".equals(searchConditions.get(Column.AUDIT_STATUS)
                        .toString().trim())
                && !"null".equals(searchConditions.get(Column.AUDIT_STATUS)
                        .toString().trim())) {
            context.numericEqual(STATUS_CHANGE_AUDIT, Column.AUDIT_STATUS,
                    searchConditions.get(Column.AUDIT_STATUS).toString());
        }
        // 院系
        if (searchConditions.get("school") != null
                && !"".equals(searchConditions.get("school").toString().trim())
                && !"null".equals(
                        searchConditions.get("school").toString().trim())) {
            context.numericEqual(UNIT, Column.META_SCHOOL_CODE,
                    searchConditions.get("school").toString());
        }
        // 专业
        if (searchConditions.get("department") != null
                && !"".equals(
                        searchConditions.get("department").toString().trim())
                && !"null".equals(
                        searchConditions.get("department").toString().trim())) {
            context.numericEqual(UNIT, Column.META_DEPARTMENT_CODE,
                    searchConditions.get("department").toString());
        }
    }

    @Override
    protected void generateSearchContext(SearchContext context) {
        assert preProcessor == null;
        context.setPrimaryTable(Table.STATUS_CHANGE_AUDIT);
        StatusChangeAuditMapper mapper = new StatusChangeAuditMapper(
                context.getMapper());
        mapper.setRepository(staffRepository, studentRepository);
        context.setMapper(mapper);
        // 找对应的申请流程步骤
        context.join(Table.AUDIT_WORKFLOW, Column.ID, STATUS_CHANGE_AUDIT,
                Column.AUDIT_WORKFLOW);
        // 查找审核链对应步骤的角色
        context.join(Table.ROLE, Column.ID, Table.AUDIT_WORKFLOW, Column.ROLE);

        // 查找审批人
        context.join(Table.ACCOUNT, "auditor", Column.ID,
                Table.STATUS_CHANGE_AUDIT, Column.ACCOUNT);

        context.addColumn("auditor", Column.STAR);
        context.addColumn(Table.STATUS_CHANGE_AUDIT, Column.STAR);
        context.addColumn(Table.STATUS_CHANGE_REQUEST, Column.STAR);
        context.addColumn(Table.AUDIT_WORKFLOW, Column.STAR);
        context.addColumn(Table.ROLE, Column.STAR);
        context.addColumn(Table.STATUS_CHANGE_TYPE, Column.MAJOR);
        context.addColumn(Table.STATUS_CHANGE_TYPE, Column.MINOR);
        context.addColumn(Table.STUDENT, Column.SNO);

    }
}
