package org.cmc.curtaincall.web.security.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class SignupRequest {

    @NotEmpty
    @Size(min = 2, max = 15)
    @Pattern(regexp = "^[0-9a-zA-Zㄱ-ㅎ가-힣]*$")  // 한글, 영문, 숫자 자유롭게 조합
    private String nickname;
}
