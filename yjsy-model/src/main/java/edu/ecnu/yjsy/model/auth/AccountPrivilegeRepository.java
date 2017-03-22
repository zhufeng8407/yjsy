package edu.ecnu.yjsy.model.auth;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface AccountPrivilegeRepository
        extends JpaRepository<AccountPrivilege, Long> {

    Set<AccountPrivilege> findByAccount(Account account);

    /**
     * 用于构建用户能够访问到的页面，主要是<code>PageService</code>中权限相关的两个地方用到。
     */
    @EntityGraph(value = "accountPrivilege.all", type = EntityGraphType.FETCH)
    @Query("select distinct ap from AccountPrivilege ap where account=?1 and role=?2")
    public List<AccountPrivilege> findAllByAccountAndRole(Account account,
            Role role);

    @Query(value = "from AccountPrivilege where role = ?1")
    public List<AccountPrivilege> findAllAccountByRole(long roleId);

    @Query("from AccountPrivilege where account=?1 and role=?2")
    public List<AccountPrivilege> findByAccountAndRole(Account account,
            Role role);

    @Query("from AccountPrivilege where role=?1")
    public List<AccountPrivilege> findByUniversity(Role role);

    @Query("from AccountPrivilege where schoolCode=?1 and role=?2")
    public List<AccountPrivilege> findBySchoolAndRole(String schoolCode,
            Role role);

    @Query("from AccountPrivilege where departmentCode=?1 and role=?2")
    public List<AccountPrivilege> findByDepartmentAndRole(String department,
            Role role);

    @Modifying
    @Transactional
    public void deleteByAccountId(long accountId);

    @Modifying
    @Transactional
    public void deleteByRoleId(long roleId);

    @Modifying
    @Transactional
    public void deleteByAccountAndRole(Account account, Role role);

}
