package edu.ecnu.yjsy.model.change;

import java.util.List;

/**
 * 用于展示异动类型的辅助类，存储了每个异动大类下面的所有异动小类。
 * 
 * @author wanglei
 */

public class StatusChangeTypeCombine {

    private String name;
    private int count;
    private List<StatusChangeType> statusChangeTypes;
    private List<String> auditWorkflow;

    public List<String> getAuditWorkflow() {
        return auditWorkflow;
    }

    public void setAuditWorkflow(List<String> auditWorkflow) {
        this.auditWorkflow = auditWorkflow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<StatusChangeType> getStatusChangeTypes() {
        return statusChangeTypes;
    }

    public void setStatusChangeTypes(List<StatusChangeType> statusChangeTypes) {
        this.statusChangeTypes = statusChangeTypes;
    }

}
