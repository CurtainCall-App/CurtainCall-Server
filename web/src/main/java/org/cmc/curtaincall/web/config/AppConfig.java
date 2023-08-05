package org.cmc.curtaincall.web.config;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

@Configuration
@EntityScan("org.cmc.curtaincall.domain")
@EnableJpaRepositories("org.cmc.curtaincall.domain")
@ComponentScan(value = {"org.cmc.curtaincall.domain"})
public class AppConfig {

    @PostConstruct
    void postConstruct() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}
