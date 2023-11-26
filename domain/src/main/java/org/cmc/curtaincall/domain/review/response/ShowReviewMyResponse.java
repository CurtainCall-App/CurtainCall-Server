package org.cmc.curtaincall.domain.review.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.cmc.curtaincall.domain.show.ShowId;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
public class ShowReviewMyResponse {

    private Long id;
    private ShowId showId;
    private String showName;
    private Integer grade;
    private String content;
    private LocalDateTime createdAt;
    private Integer likeCount;

    @Builder
    @QueryProjection
    public ShowReviewMyResponse(
            final Long id,
            final ShowId showId,
            final String showName,
            final Integer grade,
            final String content,
            final LocalDateTime createdAt,
            final Integer likeCount
    ) {
        this.id = Objects.requireNonNull(id);
        this.showId = Objects.requireNonNull(showId);
        this.showName = Objects.requireNonNull(showName);
        this.grade = Objects.requireNonNull(grade);
        this.content = Objects.requireNonNull(content);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.likeCount = Objects.requireNonNull(likeCount);
    }
}
