package edu.ecnu.yjsy.conf;

import org.springframework.context.annotation.ComponentScan;

/**
 * 通过这个地方引入security的模块
 * Created by xiafan on 16-12-13.
 */
@ComponentScan("edu.ecnu.yjsy.security")
public class SecurityModuleConfig {
}
