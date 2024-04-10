package org.cmc.curtaincall.web.show.config;

import org.cmc.curtaincall.web.show.infra.KopisBoxOfficeService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KopisProperties.class)
public class BoxOfficeConfig {

    @Bean
    public KopisBoxOfficeService kopisBoxOfficeService(KopisProperties properties) {
        return new KopisBoxOfficeService(properties.getBaseUrl(), properties.getServiceKey());
    }
}
