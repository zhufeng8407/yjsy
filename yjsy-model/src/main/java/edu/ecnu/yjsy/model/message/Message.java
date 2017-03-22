package edu.ecnu.yjsy.model.message;

import static edu.ecnu.yjsy.model.constant.Column.*;
import static edu.ecnu.yjsy.model.constant.Table.MESSAGE;
import static edu.ecnu.yjsy.model.constant.Table.MESSAGE_ACCOUNT;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import edu.ecnu.yjsy.model.EntityId;
import edu.ecnu.yjsy.model.auth.Account;

@Entity
@Table(name = MESSAGE)
public class Message extends EntityId {

    private static final long serialVersionUID = 1669711683800899582L;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinTable(name = MESSAGE_ACCOUNT,
            joinColumns = { @JoinColumn(name = "messages",
                    referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "recivers",
                    referencedColumnName = "id") })
    private Set<Account> recivers = new HashSet<>();

    @Column(name = MESSAGE_TITLE)
    private String title;

    @Column(name = MESSAGE_BEGIN_TIME)
    private String beginTime;

    @Column(name = MESSAGE_END_TIME)
    private String endTime;

    @Column(name = MESSAGE_CONTENT, length = 10240)
    private String content;

    @Column(name = MESSAGE_LINK)
    private String link;

    @Column(name = MESSAGE_TYPE)
    private String type;

    @JoinColumn(name = MESSAGE_AUTHOR)
    @ManyToOne
    private Account author;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinTable(name = MESSAGE_ACCOUNT,
            joinColumns = { @JoinColumn(name = "messages",
                    referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "receivers",
                    referencedColumnName = "id") })
    private Set<Account> receivers = new HashSet<>();

    public Set<Account> getReceivers() {
        return receivers;
    }

    public void setReceivers(Set<Account> receivers) {
        this.receivers = receivers;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Account getAuthor() {
        return author;
    }

    public void setAuthor(Account author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
