package org.cmc.curtaincall.web.service.member.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberCreate {

    @NotEmpty
    @Size(min = 2, max = 15)
    @Pattern(regexp = "^[0-9a-zA-Zㄱ-ㅎ가-힣]*$")  // 한글, 영문, 숫자 자유롭게 조합
    private String nickname;
}
