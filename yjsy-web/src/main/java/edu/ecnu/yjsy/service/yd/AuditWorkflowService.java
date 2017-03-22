package edu.ecnu.yjsy.service.yd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.ecnu.yjsy.exception.BusinessException;
import edu.ecnu.yjsy.model.auth.RoleRepository;
import edu.ecnu.yjsy.model.change.AuditWorkflow;
import edu.ecnu.yjsy.model.change.AuditWorkflowRepository;
import edu.ecnu.yjsy.model.change.StatusChangeType;
import edu.ecnu.yjsy.security.exception.ErrorCode;
import edu.ecnu.yjsy.service.MetadataService;
import net.sf.json.JSONObject;

/**
 * @author wanglei
 * @author xulinhao
 */

@Service
public class AuditWorkflowService {

    @Autowired
    private AuditWorkflowRepository workflowRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private MetadataService metadataService;

    /**
     * 获取<code>StatusChangeType.minor</code>对应的审批流程。
     * 
     * @param minor
     * @return 审批流程
     */
    public List<AuditWorkflow> getWorkflowById(String id) {
        StatusChangeType type = metadataService.getStatusChangeTypeById(id);
        return workflowRepo.findByType(type);
    }

    /**
     * 获取<code>StatusChangeType.minor</code>对应的审批流程。
     * 
     * @param minor
     * @return 审批流程
     */
    public List<AuditWorkflow> getWorkflowByMinor(String minor) {
        StatusChangeType type = metadataService.getStatusChangeTypeById(minor);
        return workflowRepo.findByType(type);
    }

    /**
     * 获取<code>StatusChangeType.minor</code>对应的审批流程的角色。
     * 
     * @param minor
     * @return 审批流程的角色
     */
    public List<String> getRolesByMinor(String minor) {
        List<AuditWorkflow> flows = this.getWorkflowByMinor(minor);
        List<String> roles = new ArrayList<String>();
        for (AuditWorkflow flow : flows) {
            roles.add(flow.getRole().getName());
        }
        return roles;
    }

    /**
     * 删除<code>StatusChangeType</code>对应的审批流程。
     * 
     * @param type
     */
    public void deleteWorkflowByMinor(StatusChangeType type) {
        // 20170113 modified by zhufeng
        // List<AuditWorkflow> flows = workflowRepo.findByType(type);
        // // FIXME
        // // 在一个事务中删除
        // for (AuditWorkflow flow : flows)
        // workflowRepo.delete(flow);
        workflowRepo.deleteFlowsByType(type.getId());

    }

    // 20170113 modified by zhufeng
    @Transactional
    public void save(String auditflow) {
        // FIXME
        // 需要注释说明审批流的格式
        List<Map<String, Object>> flows = new ArrayList<Map<String, Object>>();
        Pattern pattern = Pattern.compile("\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(auditflow);
        while (matcher.find()) {
            @SuppressWarnings("unchecked")
            Map<String, Object> sc = (Map<String, Object>) JSONObject
                    .toBean(JSONObject.fromObject(matcher.group()), Map.class);
            flows.add(sc);
        }

        // FIXME
        // 为什么要先删除？
        if (flows.get(0) != null) {
            StatusChangeType statusChangeType = metadataService
                    .getStatusChangeTypeByMinor(
                            flows.get(0).get("major").toString(),
                            flows.get(0).get("minor").toString());
            try {
                this.deleteWorkflowByMinor(statusChangeType);
            } catch (DataIntegrityViolationException e) {
                throw new BusinessException(ErrorCode.E001003);
            }
        }

        // FIXME
        // 需要在一个事务中完成
        for (Map<String, Object> flow : flows) {
            AuditWorkflow workflow = new AuditWorkflow();
            workflow.setRole(roleRepo.findByName(flow.get("role").toString()));
            workflow.setSequence(
                    Short.parseShort(flow.get("sequence").toString()));
            workflow.setType(metadataService.getStatusChangeTypeByMinor(
                    flow.get("major").toString(),
                    flow.get("minor").toString()));
            workflowRepo.save(workflow);
        }
    }

}
