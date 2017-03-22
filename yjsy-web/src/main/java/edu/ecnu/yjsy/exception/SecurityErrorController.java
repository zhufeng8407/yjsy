package edu.ecnu.yjsy.exception;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Security异常处理类
 * 
 * @author freedom.zhu
 *
 */
@RestController
public class SecurityErrorController {

    @RequestMapping(value = "/securityError")
    public void error(@RequestParam String errorCode) {
        throw new BusinessException(errorCode);
    }
}
