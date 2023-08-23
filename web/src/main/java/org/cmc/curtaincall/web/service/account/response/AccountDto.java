package org.cmc.curtaincall.web.service.account.response;

import lombok.Builder;
import org.cmc.curtaincall.domain.account.Account;
import org.cmc.curtaincall.domain.member.Member;

import java.time.LocalDateTime;
import java.util.Optional;

@Builder
public record AccountDto(
        Long memberId,
        String username,
        String refreshToken,
        LocalDateTime refreshTokenExpiresAt
) {

    public static AccountDto of(Account account) {
        return AccountDto.builder()
                .memberId(Optional.ofNullable(account.getMember())
                        .map(Member::getId)
                        .orElse(null))
                .username(account.getUsername())
                .refreshToken(account.getRefreshToken())
                .refreshTokenExpiresAt(account.getRefreshTokenExpiresAt())
                .build();
    }
}
