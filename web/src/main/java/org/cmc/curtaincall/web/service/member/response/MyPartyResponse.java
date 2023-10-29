package org.cmc.curtaincall.web.service.member.response;


import jakarta.annotation.Nullable;
import lombok.Builder;
import org.cmc.curtaincall.domain.party.PartyCategory;

import java.time.LocalDateTime;

@Builder
public record MyPartyResponse(
        Long id,
        String title,
        String content,
        Integer curMemberNum,
        Integer maxMemberNum,
        @Nullable
        LocalDateTime showAt,
        LocalDateTime createdAt,
        PartyCategory category,
        Long creatorId,
        String creatorNickname,
        @Nullable
        String creatorImageUrl,
        @Nullable
        String showId,
        @Nullable
        String showName,
        @Nullable
        String showPoster,
        @Nullable
        String facilityId,
        @Nullable
        String facilityName
) {
}
