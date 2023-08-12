package org.cmc.curtaincall.web.service.party.response;


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

    private LocalDateTime showAt;

    private Long creatorId;

    private LocalDateTime createdAt;

    private String creatorNickname;

    private String creatorImageUrl;

    private String showId;

    private String showName;

    private String facilityId;

    private String facilityName;

}
