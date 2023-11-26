package org.cmc.curtaincall.web.member.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.cmc.curtaincall.domain.member.MemberWithdrawReason;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberWithdraw {

    @NotNull
    private MemberWithdrawReason reason;

    @Size(max = 500)
    @NotNull
    private String content;
}
