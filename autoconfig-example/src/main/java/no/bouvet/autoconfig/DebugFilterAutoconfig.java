package no.bouvet.autoconfig;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
@ConditionalOnProperty(name = "debug", havingValue = "true")
public class DebugFilterAutoconfig {
    @Bean
    public FilterRegistrationBean debugFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new DebugFilter());
        filterRegistrationBean.setUrlPatterns(Collections.singletonList("/*"));
        return filterRegistrationBean;
    }
}
