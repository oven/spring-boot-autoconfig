package no.bouvet.autoconfig;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.joda.time.LocalTime;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({LocalTime.class, Module.class})
public class JodaTimeAutoConfiguration {
    @Bean
    public Module module() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalTime.class, new JodaTimeSerializer());
        return module;
    }
}