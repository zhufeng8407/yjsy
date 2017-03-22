package edu.ecnu.yjsy.model.view.student;

import java.sql.Date;

import edu.ecnu.yjsy.model.student.*;

/**
 * 基于<code>Student</code>构建的一个学生简历的视图
 *
 * @author xiafan
 */
public interface StudentProfile {

    public long getId();

    public String getName();

    public String getNameEn();

    public Date getBirthdate();

    public Ethnic getEthnic();

    public Gender getGender();

    public String getSno();

    public String getPhoto();

    public DegreeLevel getDegreeLevel();

    public short getGrade();

    public Unit getUnit();

    public Discipline getDiscipline();

}
