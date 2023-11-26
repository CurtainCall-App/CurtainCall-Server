package org.cmc.curtaincall.web.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.member.MemberId;
import org.springframework.data.domain.AuditorAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class LoginMemberAuditorAware implements AuditorAware<CreatorId> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<CreatorId> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getName)
                .map(username -> {
                    try {
                        return jdbcTemplate.queryForObject("""
                                select a.member_id
                                from account a
                                join member m on m.member_id = a.member_id
                                where a.username = ? and m.use_yn
                                """, Long.class, username);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .map(MemberId::new)
                .map(CreatorId::new);
    }
}
