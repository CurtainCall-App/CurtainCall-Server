package org.cmc.curtaincall.web.security.response;

import lombok.Builder;
import org.cmc.curtaincall.domain.account.Account;
import org.cmc.curtaincall.domain.member.MemberId;

import java.util.Optional;

@Builder
public record AccountDto(
        Long memberId,
        String username
) {

    public static AccountDto of(Account account) {
        return AccountDto.builder()
                .memberId(Optional.ofNullable(account.getMemberId())
                        .map(MemberId::getId)
                        .orElse(null))
                .username(account.getUsername())
                .build();
    }
}
