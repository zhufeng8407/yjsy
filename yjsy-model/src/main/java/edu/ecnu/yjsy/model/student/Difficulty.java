package edu.ecnu.yjsy.model.student;

import static edu.ecnu.yjsy.model.constant.Column.LEVEL;
import static edu.ecnu.yjsy.model.constant.Column.STUDENT;
import static edu.ecnu.yjsy.model.constant.Column.YEAR;
import static edu.ecnu.yjsy.model.constant.Table.DIFFICULTY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.ecnu.yjsy.model.EntityId;

@Entity
@Table(name = DIFFICULTY,
        uniqueConstraints = @UniqueConstraint(columnNames = { YEAR, STUDENT }))
public class Difficulty extends EntityId {

    /**
     * 
     */
    private static final long serialVersionUID = 3295851579168707651L;

    @Column(name = YEAR, nullable = false)
    private String year;

    @Column(name = LEVEL)
    private boolean isDifficulty;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = STUDENT, nullable = false)
    private Student student;

    // --------------

    public Difficulty() {}

    public Difficulty(String year, boolean isDifficulty, Student student) {
        super();
        this.year = year;
        this.isDifficulty = isDifficulty;
        this.student = student;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public boolean isDifficulty() {
        return isDifficulty;
    }

    public void setDifficulty(boolean isDifficulty) {
        this.isDifficulty = isDifficulty;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

}
