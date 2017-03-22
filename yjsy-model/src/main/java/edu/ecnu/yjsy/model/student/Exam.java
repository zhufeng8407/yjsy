package edu.ecnu.yjsy.model.student;

import static edu.ecnu.yjsy.model.constant.Column.*;
import static edu.ecnu.yjsy.model.constant.Table.EXAM;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import edu.ecnu.yjsy.model.EntityId;

/**
 * <code>Exam</code> is used to produce a test when students do registration at
 * department each year. Only the students who successfuly pass this exam will
 * be regarded as a complete registration.
 * 
 * TODO:
 * 
 * 1. if we need to add a exam score table to record each student's scores for
 * different years
 * 
 * 2. if we need to add the levels for each test such as easy, normal and hard
 * 
 * @author guhang
 */

@Entity
@Table(name = EXAM,
        uniqueConstraints = @UniqueConstraint(columnNames = { TEST }))
public class Exam extends EntityId {

    /**
     * 
     */
    private static final long serialVersionUID = 6071412275483904266L;

    @Column(name = TEST, length = 200, nullable = false)
    private String test;

    @Column(name = OPTION_A, length = 100, nullable = false)
    private String op1;

    @Column(name = OPTION_B, length = 100, nullable = false)
    private String op2;

    @Column(name = OPTION_C, length = 100, nullable = false)
    private String op3;

    @Column(name = OPTION_D, length = 100, nullable = false)
    private String op4;

    @Column(name = ANSWER, length = 2, nullable = false)
    private String answer;

    @Column(name = TOTAL, nullable = false)
    private long total;

    @Column(name = TOTAL_CORRECT, nullable = false)
    private long totalCorrect;

    public Exam() {}

    public Exam(String test, String op1, String op2, String op3, String op4,
            String answer, long total, long totalCorrect) {
        super();
        this.test = test;
        this.op1 = op1;
        this.op2 = op2;
        this.op3 = op3;
        this.op4 = op4;
        this.answer = answer;
        this.total = total;
        this.totalCorrect = totalCorrect;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getOp1() {
        return op1;
    }

    public void setOp1(String op1) {
        this.op1 = op1;
    }

    public String getOp2() {
        return op2;
    }

    public void setOp2(String op2) {
        this.op2 = op2;
    }

    public String getOp3() {
        return op3;
    }

    public void setOp3(String op3) {
        this.op3 = op3;
    }

    public String getOp4() {
        return op4;
    }

    public void setOp4(String op4) {
        this.op4 = op4;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long gettotalCorrect() {
        return totalCorrect;
    }

    public void settotalCorrect(long totalCorrect) {
        this.totalCorrect = totalCorrect;
    }

    // FIXME
    // use Column constants to build string
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(ID).append(": ").append(getId()).append(" ");
        sb.append(TEST).append(": ").append(test).append(" ");
        sb.append(OPTION_A).append(": ").append(op1).append(" ");
        sb.append(OPTION_B).append(": ").append(op2).append(" ");
        sb.append(OPTION_C).append(": ").append(op3).append(" ");
        sb.append(OPTION_D).append(": ").append(op4).append(" ");
        sb.append(ANSWER).append(": ").append(answer).append(" ");
        sb.append(TOTAL).append(": ").append(total).append(" ");
        sb.append(TOTAL_CORRECT).append(": ").append(totalCorrect + '\n');

        return sb.toString();
    }

}
