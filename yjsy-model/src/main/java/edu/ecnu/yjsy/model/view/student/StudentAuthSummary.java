package edu.ecnu.yjsy.model.view.student;

import edu.ecnu.yjsy.model.staff.Staff;
import edu.ecnu.yjsy.model.student.Unit;

/**
 * Created by xiafan on 17-2-8.
 */
public interface StudentAuthSummary {

    public long getId();

    public Unit getUnit();

    public Staff getSupervisor();
}
