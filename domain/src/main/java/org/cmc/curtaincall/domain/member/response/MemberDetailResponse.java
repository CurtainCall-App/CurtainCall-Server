package org.cmc.curtaincall.domain.member.response;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.cmc.curtaincall.domain.member.MemberId;

@Getter
@ToString
public class MemberDetailResponse {

    private MemberId id;

    private String nickname;

    @Nullable
    private Long imageId;

    @Nullable
    private String imageUrl;

    private Long recruitingNum;

    private Long participationNum;

    @Builder
    public MemberDetailResponse(
            final MemberId id,
            final String nickname,
            @Nullable final Long imageId,
            @Nullable final String imageUrl,
            final Long recruitingNum,
            final Long participationNum
    ) {
        this.id = id;
        this.nickname = nickname;
        this.imageId = imageId;
        this.imageUrl = imageUrl;
        this.recruitingNum = recruitingNum;
        this.participationNum = participationNum;
    }
}
