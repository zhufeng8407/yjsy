package edu.ecnu.yjsy.model.change;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

@Component
public class StatusChangeAuditDaoImpl {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findByRequestIdAndFlowId(long id,
            long workflowId) {
        try {
            Query query = entityManager.createNativeQuery(
                    "select t_xj.xm as name, t_xj.xh as sno, t_xj.nj as grade, m_yx.yxmc as school, "
                            + " m_zy.zymc as department, DATE_FORMAT(t_yd_sq.cjrq, '%Y-%m-%d %H:%i:%S') as requestDate, t_yd_sq.dsqrxm as staff, "
                            + " t_yd_sq.ly as reason,  case t_yd_sp.spzt when '0' then '同意' when '1' then '否决' "
                            + " when '2' then '审批中' when '3' then '待审批' end as auditStatus, "
                            + " case t_yd_sp.spzt when '0' then 'glyphicon-ok' when '1' then 'glyphicon-remove' "
                            + " when '2' then 'glyphicon-edit' when '3' then 'glyphicon-time' end as icon, "
                            + " case t_yd_sp.spzt when '0' then 'success' when '1' then 'danger' "
                            + " when '2' then 'info' when '3' then 'warning' end as class, "
                            + " t_role.name as auditRoleName "
                            + " from t_yd_sp  join t_account as auditor on auditor.id = t_yd_sp.fk_zh "
                            + " join t_yd_lc on t_yd_lc.id = t_yd_sp.fk_yd_lc "
                            + " join t_role on t_role.id = t_yd_lc.fk_js "
                            + " join t_yd_sq on t_yd_sq.id = t_yd_sp.fk_yd_sq "
                            + " join t_xj on t_xj.id = t_yd_sq.fk_xj "
                            + " join m_yx on m_yx.id = t_xj.fk_yx "
                            + " join m_zy on m_zy.id = t_xj.fk_zy where t_yd_sq.id = ? and t_yd_sp.fk_yd_lc <= ? ");

            query.setParameter(1, id);
            query.setParameter(2, workflowId);
            query.unwrap(SQLQuery.class)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }
}
