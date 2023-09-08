package org.cmc.curtaincall.web.service.member.response;


import jakarta.annotation.Nullable;
import lombok.*;
import org.cmc.curtaincall.domain.image.Image;
import org.cmc.curtaincall.domain.party.Party;
import org.cmc.curtaincall.domain.party.PartyCategory;
import org.cmc.curtaincall.domain.show.Show;

import java.time.LocalDateTime;
import java.util.Optional;

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
    public static MyPartyResponse of(Party party) {
        Optional<Show> showOptional = Optional.ofNullable(party.getShow());
        return MyPartyResponse.builder()
                .id(party.getId())
                .title(party.getTitle())
                .content(party.getContent())
                .curMemberNum(party.getCurMemberNum())
                .maxMemberNum(party.getMaxMemberNum())
                .showAt(party.getShowAt())
                .createdAt(party.getCreatedAt())
                .category(party.getCategory())
                .creatorId(party.getCreatedBy().getId())
                .creatorNickname(party.getCreatedBy().getNickname())
                .creatorImageUrl(Optional.ofNullable(party.getCreatedBy().getImage())
                        .filter(Image::getUseYn)
                        .map(Image::getUrl)
                        .orElse(null))
                .showId(showOptional.map(Show::getId).orElse(null))
                .showName(showOptional.map(Show::getName).orElse(null))
                .showPoster(showOptional.map(Show::getPoster).orElse(null))
                .facilityId(showOptional.map(show -> show.getFacility().getId()).orElse(null))
                .facilityName(showOptional.map(show -> show.getFacility().getName()).orElse(null))
                .build();
    }
}
