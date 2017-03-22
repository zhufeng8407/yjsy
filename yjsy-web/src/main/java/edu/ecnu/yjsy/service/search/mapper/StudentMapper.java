package edu.ecnu.yjsy.service.search.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import edu.ecnu.yjsy.model.student.DisciplineRepository;
import edu.ecnu.yjsy.model.student.PregraduationStatus;
import edu.ecnu.yjsy.model.student.Student;
import edu.ecnu.yjsy.model.student.UnitRepository;

import static edu.ecnu.yjsy.model.constant.Column.*;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * FIXME: xiafan : 如果之后这个类仍然没有使用，可以考虑删除
 */
@Component
@SuppressWarnings("rawtypes")
public class StudentMapper implements RowMapper {

    @Autowired
    private UnitRepository unitRepo;

    @Autowired
    private DisciplineRepository disciplineRepo;
    
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        //FIXME: xiafan, 将查询得到的字段填充到Student对象中
        Student student = new Student();
        PregraduationStatus[] pregraduationStatus = PregraduationStatus.values();

        student.setId(rs.getLong(ID));
        student.setSno(rs.getString(SNO));
        student.setNew(rs.getBoolean(IS_NEW));
        student.setName(rs.getString(NAME));
        student.setNameEn(rs.getString(NAME_EN));
        student.setBirthdate(rs.getDate(BIRTHDATE));
        student.setSsn(rs.getString(SSN));
        student.setEmail(rs.getString(EMAIL));
        student.setAdmissionDate(rs.getDate(ADMISSION_DATE));
        student.setGrade(rs.getShort(GRADE));
        student.setTerm(rs.getShort(TERM));
        student.setDomitory(rs.getString("SS"));
        student.setExamineeNo(rs.getString(EXAMINEE_NO));
        student.setPregraduationStatus(pregraduationStatus[rs.getInt(PREGRADUATION_STATUS)]);

        //外键
        student.setUnit(unitRepo.findOne(rs.getLong(UNIT)));
        student.setDiscipline(disciplineRepo.findOne(rs.getLong(DISCIPLINE)));

        return student;
    }

}
