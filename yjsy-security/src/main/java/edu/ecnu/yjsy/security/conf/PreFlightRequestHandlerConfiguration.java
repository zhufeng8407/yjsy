package edu.ecnu.yjsy.security.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 当<code>DomainAccessDecisionManager</code>对<code>OPTION</code>处理之后，
 * 还是需要该类去进行PreFlight的拦截
 * 
 * @author guhang
 **/
@Configuration
public class PreFlightRequestHandlerConfiguration {

    @Value("${ecnu.yjsy.auth.site-domain}")
    private String siteDomain;

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin(siteDomain);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setMaxAge((long) 60 * 10);
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(
                new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }

}
