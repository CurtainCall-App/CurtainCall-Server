package org.cmc.curtaincall.web.common.config;

import jakarta.annotation.PostConstruct;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;

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

    @Bean
    public AuditorAware<CreatorId> auditorProvider(final JdbcTemplate jdbcTemplate) {
        return new LoginMemberAuditorAware(jdbcTemplate);
    }
}
