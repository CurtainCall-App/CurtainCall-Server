package org.cmc.curtaincall.domain.party.response;


import com.querydsl.core.annotations.QueryProjection;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.cmc.curtaincall.domain.party.PartyCategory;

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
    private Long creatorId;
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

    @Builder
    @QueryProjection
    public PartyParticipationResponse(
            Long id,
            String title,
            String content,
            Integer curMemberNum,
            Integer maxMemberNum,
            LocalDateTime createdAt,
            PartyCategory category,
            Long creatorId,
            String creatorNickname,
            String creatorImageUrl,
            @Nullable String showId,
            @Nullable String showName,
            @Nullable String showPoster,
            @Nullable LocalDateTime showAt,
            @Nullable String facilityId,
            @Nullable String facilityName
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