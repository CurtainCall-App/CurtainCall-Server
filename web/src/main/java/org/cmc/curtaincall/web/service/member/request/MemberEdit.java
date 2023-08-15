package org.cmc.curtaincall.web.service.member.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberEdit {

    @NotEmpty
    @Size(max = 20)
    private String nickname;

    private Long imageId;
}
