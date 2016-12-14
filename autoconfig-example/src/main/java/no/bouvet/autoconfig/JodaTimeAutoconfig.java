package no.bouvet.autoconfig;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.joda.time.LocalDate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnClass(LocalDate.class)
public class JodaTimeAutoconfig {
    @Bean
    public Module module() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDate.class, new JodaTimeSerializer());
        return module;
    }
}
