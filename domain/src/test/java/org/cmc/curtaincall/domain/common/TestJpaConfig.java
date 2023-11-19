package org.cmc.curtaincall.domain.common;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@TestConfiguration
@EnableJpaAuditing
public class TestJpaConfig {

    @Bean
    public JPAQueryFactory jpaQueryFactory(final EntityManager em) {
        return new JPAQueryFactory(em);
    }
}