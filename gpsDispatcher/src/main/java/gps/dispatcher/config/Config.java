package gps.dispatcher.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentLinkedQueue;

@Configuration
@ComponentScan("gps.dispatcher")
public class Config {

    @Bean
    public ConcurrentLinkedQueue queue() {
        return new ConcurrentLinkedQueue();
    }

}
