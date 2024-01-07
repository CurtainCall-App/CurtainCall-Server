package org.cmc.curtaincall.domain.party.response;


import com.querydsl.core.annotations.QueryProjection;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.show.FacilityId;
import org.cmc.curtaincall.domain.show.ShowId;

import java.time.LocalDateTime;

@Getter
@ToString
public class PartyDetailResponse {

    private Long id;

    private String title;

    private String content;

    private Integer curMemberNum;

    private Integer maxMemberNum;

    private CreatorId creatorId;

    private LocalDateTime createdAt;

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
    private FacilityId facilityId;

    @Nullable
    private String facilityName;

    @Builder
    @QueryProjection
    public PartyDetailResponse(
            Long id,
            String title,
            String content,
            Integer curMemberNum,
            Integer maxMemberNum,
            LocalDateTime createdAt,
            CreatorId creatorId,
            String creatorNickname,
            @Nullable String creatorImageUrl,
            @Nullable ShowId showId,
            @Nullable String showName,
            @Nullable String showPoster,
            @Nullable LocalDateTime showAt,
            @Nullable FacilityId facilityId,
            @Nullable String facilityName
    ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.curMemberNum = curMemberNum;
        this.maxMemberNum = maxMemberNum;
        this.showAt = showAt;
        this.creatorId = creatorId;
        this.createdAt = createdAt;
        this.creatorNickname = creatorNickname;
        this.creatorImageUrl = creatorImageUrl;
        this.showId = showId;
        this.showName = showName;
        this.showPoster = showPoster;
        this.facilityId = facilityId;
        this.facilityName = facilityName;
    }
}
