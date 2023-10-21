package org.cmc.curtaincall.domain.review.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class ShowReviewResponse {

    private Long id;

    private String showId;

    private Integer grade;

    private String content;

    private Long creatorId;

    private String creatorNickname;

    private String creatorImageUrl;

    private LocalDateTime createdAt;

    private Integer likeCount;

    @QueryProjection
    public ShowReviewResponse(
            Long id,
            String showId,
            Integer grade,
            String content,
            Long creatorId,
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
