package edu.ecnu.yjsy.util;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import edu.ecnu.yjsy.service.xj.CheckinService;
import edu.ecnu.yjsy.service.xj.DifficultyService;
import edu.ecnu.yjsy.service.xj.FeeService;
import edu.ecnu.yjsy.service.xj.StudentService;

/**
 * 加载学籍相关的息。
 *
 * @author xulinhao
 */

@Service
public class DataUtil {

    private static final Logger LOG = LoggerFactory.getLogger(DataUtil.class);

    private static final String PATH = "data";

    // private static final String FRESHMAN = PATH + "/学籍-全日制-新生.xlsx";
    // private static final String FRESHMAN_DIFFICULTY = PATH +
    // "/助困-全日制-新生.xlsx";
    // private static final String FRESHMAN_CHECKIN = PATH + "/报到-全日制-新生.xlsx";
    // private static final String FRESHMAN_FEE = PATH + "/缴费-全日制-新生.xlsx";

    private static final String ENROLLED = PATH + "/学籍-全日制-在校生.xlsx";
    private static final String ENROLLED_DIFFICULTY = PATH + "/助困-全日制-在校生.xlsx";
    private static final String ENROLLED_CHECKIN = PATH + "/报到-全日制-在校生.xlsx";
    private static final String ENROLLED_FEE = PATH + "/缴费-全日制-在校生.xlsx";

    private static final String GRADUATE = PATH + "/学籍-全日制-离校生.xlsx";
    private static final String GRADUATE_DIFFICULTY = PATH + "/助困-全日制-离校生.xlsx";
    private static final String GRADUATE_CHECKIN = PATH + "/报到-全日制-离校生.xlsx";
    private static final String GRADUATE_FEE = PATH + "/缴费-全日制-离校生.xlsx";

    @Autowired
    private StudentService studentService;

    @Autowired
    private CheckinService checkinService;

    @Autowired
    private FeeService feeService;

    @Autowired
    private DifficultyService difficultyService;

    public void load() {
        // this.loadFreshman();
        this.loadEnrolled();
        this.loadGraduate();
    }

    // private void loadFreshman() {
    // this.loadStduent(FRESHMAN);
    // this.loadDifficulty(FRESHMAN_DIFFICULTY);
    // this.loadCheckin(FRESHMAN_CHECKIN);
    // this.loadFee(FRESHMAN_FEE);
    // }

    private void loadEnrolled() {
        this.loadStduent(ENROLLED);
        this.loadDifficulty(ENROLLED_DIFFICULTY);
        this.loadCheckin(ENROLLED_CHECKIN);
        this.loadFee(ENROLLED_FEE);
    }

    private void loadGraduate() {
        this.loadStduent(GRADUATE);
        this.loadDifficulty(GRADUATE_DIFFICULTY);
        this.loadCheckin(GRADUATE_CHECKIN);
        this.loadFee(GRADUATE_FEE);
    }

    private ClassPathResource exist(String file) {
        ClassPathResource resource = new ClassPathResource(file);
        if (!resource.exists()) {
            LOG.error("Not found {}", file);
            return null;
        }
        return resource;
    }

    private void loadStduent(String file) {
        try {
            ClassPathResource resource = exist(file);
            if (resource != null) {
                studentService.upload(resource.getInputStream(),
                        FilenameUtils.getExtension(resource.getFilename()));
            }
        } catch (Exception e) {
            throw new RuntimeException("加载学籍数据失败" + e.getMessage());
        }
    }

    private void loadDifficulty(String file) {
        try {
            ClassPathResource resource = exist(file);
            if (resource != null) {
                difficultyService.upload(resource.getInputStream(), "");
            }
        } catch (Exception e) {
            throw new RuntimeException("加载助困数据失败" + e.getMessage());
        }
    }

    private void loadCheckin(String file) {
        try {
            ClassPathResource resource = exist(file);
            if (resource != null) {
                checkinService.upload(resource.getInputStream(), "", false);
            }
        } catch (Exception e) {
            throw new RuntimeException("加载报到数据失败" + e.getMessage());
        }
    }

    private void loadFee(String file) {
        try {
            ClassPathResource resource = exist(file);
            if (resource != null) {
                feeService.upload(resource.getInputStream(), "", false);
            }
        } catch (Exception e) {
            throw new RuntimeException("加载缴费数据失败" + e.getMessage());
        }
    }

}
