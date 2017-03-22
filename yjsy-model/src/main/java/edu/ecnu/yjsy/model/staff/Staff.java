package edu.ecnu.yjsy.model.staff;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.ecnu.yjsy.model.EntityId;
import edu.ecnu.yjsy.model.student.Student;
import edu.ecnu.yjsy.model.student.Unit;

import javax.persistence.Column;
import javax.persistence.*;
import javax.persistence.Table;

import java.util.HashSet;
import java.util.Set;

import static edu.ecnu.yjsy.model.constant.Column.STAFF_SNO;
import static edu.ecnu.yjsy.model.constant.Table.STAFF;

/**
 * Staff includes administrative staffs and eduational staffs, where educational
 * staffs can be further divided into common staffs, supervisors and academic
 * committee staffs.
 *
 * @author xiafan
 * @author wanglei
 * @author xulinhao
 */

@Entity
@Table(name = STAFF,
        uniqueConstraints = @UniqueConstraint(columnNames = { STAFF_SNO }))
public class Staff extends EntityId {

    private static final long serialVersionUID = -4544737095876892150L;

    @Column(name = STAFF_SNO, nullable = false, length = 10)
    private String sno;

    @Column(nullable = false, length = 20)
    private String name;

    // FIXME: enumeration
    @Column(length = 20)
    private String profession;

    @Column(length = 40)
    private String email;

    @Column(length = 20)
    private String wechat;

    @Column(length = 20)
    private String telephone;

    private boolean superviseMaster;

    private boolean superviseDoctor;

    @JsonIgnore
    @OneToMany(mappedBy = "supervisor")
    private Set<Student> students = new HashSet<>();

    // XXX
    // one staff could belong to multiple units, where one unit is solid
    // and the others are void
    // FIXME: 不明白什么不加jsonignore，居然在向前端序列化的时候，居然不会抛lazy
    // initialization的异常,解决这个问题，还是需要在Repository里避免查处这个字段
    @JsonIgnore
    @ManyToMany
    private Set<Unit> units = new HashSet<>();

    // --------------------
    // METHODS
    // --------------------

    public Staff() {}

    public Staff(long id, String name) {
        super.setId(id);
        this.name = name;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public boolean isSuperviseMaster() {
        return superviseMaster;
    }

    public void setSuperviseMaster(boolean superviseMaster) {
        this.superviseMaster = superviseMaster;
    }

    public boolean isSuperviseDoctor() {
        return superviseDoctor;
    }

    public void setSuperviseDoctor(boolean superviseDoctor) {
        this.superviseDoctor = superviseDoctor;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Set<Unit> getUnits() {
        return units;
    }

    public void setUnits(Set<Unit> units) {
        this.units = units;
    }

    @Override
    public String toString() {
        return name;
    }
}
