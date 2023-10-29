package org.cmc.curtaincall.web.party.response;


import jakarta.annotation.Nullable;
import lombok.*;
import org.cmc.curtaincall.domain.party.PartyCategory;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PartyDetailResponse {

    private Long id;

    private String title;

    private String content;

    private PartyCategory category;

    private Integer curMemberNum;

    private Integer maxMemberNum;

    @Nullable
    private LocalDateTime showAt;

    private Long creatorId;

    private LocalDateTime createdAt;

    private String creatorNickname;

    @Nullable
    private String creatorImageUrl;

    @Nullable
    private String showId;

    @Nullable
    private String showName;

    @Nullable
    private String showPoster;

    @Nullable
    private String facilityId;

    @Nullable
    private String facilityName;

}
