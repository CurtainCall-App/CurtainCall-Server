package org.cmc.curtaincall.batch.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("org.cmc.curtaincall.domain")
@EnableJpaRepositories("org.cmc.curtaincall.domain")
@ComponentScan(value = {"org.cmc.curtaincall.batch", "org.cmc.curtaincall.domain"})
@Configuration
public class AppConfig {
}
