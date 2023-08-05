package org.cmc.curtaincall.web.service.member.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberCreate {

    @NotEmpty
    private String nickname;
}
