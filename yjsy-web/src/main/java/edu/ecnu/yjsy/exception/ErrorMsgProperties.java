package edu.ecnu.yjsy.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * 读取报错信息配置class
 * @author freedom.zhu
 *
 */
@ComponentScan("edu.ecnu.yjsy.exception")
@Configuration
@PropertySource(value="classpath:errorMsg.properties", encoding="UTF-8" )
public class ErrorMsgProperties {
    @Autowired  
    private Environment env;  
    
    public String getProperty(String key){
        return env.getProperty(key);  
    }  
}
