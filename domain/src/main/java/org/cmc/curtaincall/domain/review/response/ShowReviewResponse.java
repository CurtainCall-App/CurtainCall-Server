package org.cmc.curtaincall.domain.review.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.show.ShowId;

import java.time.LocalDateTime;

@Getter
@ToString
public class ShowReviewResponse {

    private Long id;
    private ShowId showId;
    private Integer grade;
    private String content;
    private CreatorId creatorId;
    private String creatorNickname;
    private String creatorImageUrl;
    private LocalDateTime createdAt;
    private Integer likeCount;

    @Builder
    @QueryProjection
    public ShowReviewResponse(
            Long id,
            ShowId showId,
            Integer grade,
            String content,
            CreatorId creatorId,
            String creatorNickname,
            String creatorImageUrl,
            LocalDateTime createdAt,
            Integer likeCount
    ) {
        this.id = id;
        this.showId = showId;
        this.grade = grade;
        this.content = content;
        this.creatorId = creatorId;
        this.creatorNickname = creatorNickname;
        this.creatorImageUrl = creatorImageUrl;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
    }
}
