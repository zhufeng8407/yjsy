package edu.ecnu.yjsy.security.authentication;

import org.springframework.security.core.GrantedAuthority;

import edu.ecnu.yjsy.model.auth.AccountPrivilege;

/**
 * 基于角色的<code>Spring Security</code>的<code>GrantedAuthority</code>的自定义实现，这样权限管控模块能够获取到
 * 用户在每个角色下的具体权限
 *
 * @author xiafan
 */
public class GrantedDomainAuthority implements GrantedAuthority {

    private static final long serialVersionUID = -5464556834147826641L;

    private AccountPrivilege accountPrivilege;

    public GrantedDomainAuthority() {}

    public GrantedDomainAuthority(AccountPrivilege accountPrivilege) {
        this.accountPrivilege = accountPrivilege;
    }

    @Override
    public String getAuthority() {
        return accountPrivilege.toString();
    }

    public AccountPrivilege getAccountPrivilege() {
        return accountPrivilege;
    }

    public void setAccountPrivilege(AccountPrivilege accountPrivilege) {
        this.accountPrivilege = accountPrivilege;
    }

    @Override
    public int hashCode() {
        return accountPrivilege.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GrantedDomainAuthority) {
            GrantedDomainAuthority gda = (GrantedDomainAuthority) obj;
            return accountPrivilege.equals(gda.accountPrivilege);
        } else if (obj instanceof AccountPrivilege) {
            return accountPrivilege.equals(((AccountPrivilege) obj));
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return accountPrivilege.toString();
    }

}
