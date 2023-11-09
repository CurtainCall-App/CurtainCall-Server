package org.cmc.curtaincall.web.member.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.cmc.curtaincall.domain.member.MemberDeleteReason;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberDelete {

    @NotNull
    private MemberDeleteReason reason;

    @Size(max = 500)
    @NotNull
    private String content;
}
