package org.cmc.curtaincall.domain.party.response;


import com.querydsl.core.annotations.QueryProjection;
import jakarta.annotation.Nullable;
import lombok.*;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.party.PartyCategory;
import org.cmc.curtaincall.domain.show.ShowId;

import java.time.LocalDateTime;

@Getter
@ToString
public class PartyResponse {

    private Long id;

    private String title;

    private Integer curMemberNum;

    private Integer maxMemberNum;

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
    private LocalDateTime showAt;

    @Nullable
    private String facilityId;

    @Nullable
    private String facilityName;

    @Builder
    @QueryProjection
    public PartyResponse(
            Long id,
            String title,
            Integer curMemberNum,
            Integer maxMemberNum,
            LocalDateTime createdAt,
            PartyCategory category,
            CreatorId creatorId,
            String creatorNickname,
            @Nullable String creatorImageUrl,
            @Nullable ShowId showId,
            @Nullable String showName,
            @Nullable String showPoster,
            @Nullable LocalDateTime showAt,
            @Nullable String facilityId,
            @Nullable String facilityName
    ) {
        this.id = id;
        this.title = title;
        this.curMemberNum = curMemberNum;
        this.maxMemberNum = maxMemberNum;
        this.createdAt = createdAt;
        this.category = category;
        this.creatorId = creatorId;
        this.creatorNickname = creatorNickname;
        this.creatorImageUrl = creatorImageUrl;
        this.showId = showId;
        this.showName = showName;
        this.showPoster = showPoster;
        this.showAt = showAt;
        this.facilityId = facilityId;
        this.facilityName = facilityName;
    }
}
