package edu.ecnu.yjsy.model.student;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.ecnu.yjsy.model.EntityId;

import javax.persistence.*;
import java.sql.Date;

import static edu.ecnu.yjsy.model.constant.Column.*;
import static edu.ecnu.yjsy.model.constant.Table.REGISTRATION;

/**
 * 报到，注册和缴费的流水信息。
 *
 * @author songshubin
 * @author xiafan
 * @author xulinhao
 */

@Entity
@Table(name = REGISTRATION)
public class Registration extends EntityId {

    /**
     * 
     */
    private static final long serialVersionUID = -636348588505767334L;

    // 报到
    @Column(name = CHECKIN)
    private boolean checkin;

    // 报到日期
    @Column(name = CHECKIN_DATE, length = 8)
    private Date checkinDate;

    // 报到码
    @Column(name = CHECKIN_NO, length = 20)
    private String checkinNo;

    // 注冊
    @Column(name = REGISTER)
    private boolean register;

    // 注册日期
    @Column(name = REGISTER_DATE, length = 8)
    private Date registerDate;

    // 缴费
    @Column(name = FEE)
    private boolean fee;

    // 缴费日期
    @Column(name = FEE_DATE, length = 8)
    private Date feeDate;

    // 应缴学费
    @Column(name = FEE_SHOULD_PAID)
    private double feeShouldPaid;

    // 实缴学费
    @Column(name = FEE_PAID)
    private double feePaid;

    // 学年
    @Column(name = YEAR)
    private String year;

    // 学期
    @Column(name = TERM)
    private int term;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = STUDENT, nullable = false)
    private Student student;

    // --------------------
    // METHODS
    // --------------------

    public Registration() {}

    public Registration(boolean checkin, Date checkinDate, String checkinNo,
            boolean register, Date registerDate, boolean fee, Date feeDate,
            double feePaid, String year, int term, Student student) {
        super();
        this.checkin = checkin;
        this.checkinDate = checkinDate;
        this.checkinNo = checkinNo;
        this.register = register;
        this.registerDate = registerDate;
        this.fee = fee;
        this.feeDate = feeDate;
        this.feePaid = feePaid;
        this.year = year;
        this.term = term;
        this.student = student;
    }

    public boolean isCheckin() {
        return checkin;
    }

    public void setCheckin(boolean checkin) {
        this.checkin = checkin;
    }

    public Date getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(Date checkinDate) {
        this.checkinDate = checkinDate;
    }

    public String getCheckinNo() {
        return checkinNo;
    }

    public void setCheckinNo(String checkinNo) {
        this.checkinNo = checkinNo;
    }

    public boolean isRegister() {
        return register;
    }

    public void setRegister(boolean register) {
        this.register = register;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public boolean isFee() {
        return fee;
    }

    public void setFee(boolean fee) {
        this.fee = fee;
    }

    public double getFeePaid() {
        return feePaid;
    }

    public void setFeePaid(double feePaid) {
        this.feePaid = feePaid;
    }

    public double getFeeShouldPaid() {
        return feeShouldPaid;
    }

    public void setFeeShouldPaid(double feeShouldPaid) {
        this.feeShouldPaid = feeShouldPaid;
    }

    public Date getFeeDate() {
        return feeDate;
    }

    public void setFeeDate(Date feeDate) {
        this.feeDate = feeDate;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

}
