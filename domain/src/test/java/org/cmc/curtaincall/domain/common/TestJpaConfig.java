package org.cmc.curtaincall.domain.common;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.member.Member;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@TestConfiguration
@EnableJpaAuditing
@RequiredArgsConstructor
public class TestJpaConfig {

    private final EntityManager em;

    public static final long AUDITOR_MEMBER_ID = 100L;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(em);
    }

    @Bean
    public AuditorAware<Member> auditorProvider() {
        return () -> Optional.of(new Member(AUDITOR_MEMBER_ID));
    }
}