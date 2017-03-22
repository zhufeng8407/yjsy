package edu.ecnu.yjsy.conf;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("edu.ecnu.yjsy.model")
@EnableJpaRepositories("edu.ecnu.yjsy.model")
public class ModelConfig {

}
