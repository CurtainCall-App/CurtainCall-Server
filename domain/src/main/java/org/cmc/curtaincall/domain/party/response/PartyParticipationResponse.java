package org.cmc.curtaincall.domain.party.response;


import com.querydsl.core.annotations.QueryProjection;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.party.PartyCategory;
import org.cmc.curtaincall.domain.show.FacilityId;
import org.cmc.curtaincall.domain.show.ShowId;

import java.time.LocalDateTime;

@Getter
@ToString
public class PartyParticipationResponse {

    private Long id;
    private String title;
    private String content;
    private Integer curMemberNum;
    private Integer maxMemberNum;
    @Nullable
    private LocalDateTime showAt;
    private LocalDateTime createdAt;
    private PartyCategory category;
    private CreatorId creatorId;
    private String creatorNickname;
    @Nullable
    private String creatorImageUrl;
    @Nullable
    private ShowId showId;
    @Nullable
    private String showName;
    @Nullable
    private String showPoster;
    @Nullable
    private FacilityId facilityId;
    @Nullable
    private String facilityName;

    @Builder
    @QueryProjection
    public PartyParticipationResponse(
            final Long id,
            final String title,
            final String content,
            final Integer curMemberNum,
            final Integer maxMemberNum,
            final LocalDateTime createdAt,
            final PartyCategory category,
            final CreatorId creatorId,
            final String creatorNickname,
            final String creatorImageUrl,
            @Nullable final ShowId showId,
            @Nullable final String showName,
            @Nullable final String showPoster,
            @Nullable final LocalDateTime showAt,
            @Nullable final FacilityId facilityId,
            @Nullable final String facilityName
    ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.curMemberNum = curMemberNum;
        this.maxMemberNum = maxMemberNum;
        this.showAt = showAt;
        this.createdAt = createdAt;
        this.category = category;
        this.creatorId = creatorId;
        this.creatorNickname = creatorNickname;
        this.creatorImageUrl = creatorImageUrl;
        this.showId = showId;
        this.showName = showName;
        this.showPoster = showPoster;
        this.facilityId = facilityId;
        this.facilityName = facilityName;
    }
}