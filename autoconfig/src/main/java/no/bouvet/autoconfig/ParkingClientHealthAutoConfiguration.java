package no.bouvet.autoconfig;

import no.bouvet.parking.ParkingClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({ParkingClient.class, HealthIndicator.class})
@ConditionalOnBean(ParkingClient.class)
public class ParkingClientHealthAutoConfiguration {
    
    @Bean
    @Autowired
    HealthIndicator parkingClientHealthIndicator(ParkingClient parkingClient) {
        return new ParkingClientHealthIndicator(parkingClient);
    }
}
