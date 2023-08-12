package org.cmc.curtaincall.web.service.party.response;


import lombok.*;
import org.cmc.curtaincall.domain.party.PartyCategory;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PartyResponse {

    private Long id;

    private String title;

    private Integer curMemberNum;

    private Integer maxMemberNum;

    private LocalDateTime showAt;

    private LocalDateTime createdAt;

    private PartyCategory category;

    private Long creatorId;

    private String creatorNickname;

    private String creatorImageUrl;

    private String showId;

    private String showName;

    private String showPoster;

    private String facilityId;

    private String facilityName;

}
