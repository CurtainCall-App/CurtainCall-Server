package org.cmc.curtaincall.web.service.member.response;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberDetailResponse {

    private Long id;

    private String nickname;

    private Long imageId;

    private String imageUrl;

    private Long recruitingNum;

    private Long participationNum;
}
