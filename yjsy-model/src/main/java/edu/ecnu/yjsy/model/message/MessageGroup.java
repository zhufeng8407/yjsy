package edu.ecnu.yjsy.model.message;

import static edu.ecnu.yjsy.model.constant.Column.*;
import static edu.ecnu.yjsy.model.constant.Table.MESSAGEGROUP;
import static edu.ecnu.yjsy.model.constant.Table.MESSAGEGROUP_MEMBER;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import edu.ecnu.yjsy.model.EntityId;
import edu.ecnu.yjsy.model.auth.Account;

@Entity
@Table(name = MESSAGEGROUP, uniqueConstraints = @UniqueConstraint(columnNames = {
        MESSAGE_GROUP_NAME }))
public class MessageGroup extends EntityId {

    private static final long serialVersionUID = -7453298075039116818L;

    @Column(name = MESSAGE_GROUP_NAME)
    private String name;

    @ManyToMany
    @JoinTable(name = MESSAGEGROUP_MEMBER)
    private Set<Account> members = new HashSet<Account>();

    @JoinColumn(name = MESSAGE_GROUP_OWNER)
    @ManyToOne
    private Account owner;

    public MessageGroup() {

    }

    public MessageGroup(long id, String name) {
        super.setId(id);
        ;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Account> getMembers() {
        return members;
    }

    public void setMembers(Set<Account> members) {
        this.members = members;
    }

    public void addMember(Account member) {
        this.members.add(member);
    }

    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

}
