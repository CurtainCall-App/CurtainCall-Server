package org.cmc.curtaincall.domain.review;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseTimeEntity;
import org.cmc.curtaincall.domain.review.exception.ShowReviewUnableToCancelReviewException;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.domain.show.ShowState;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "show_review_stats",
        indexes = {
                @Index(name = "IX_show_review_stats__genre_review_grade_avg",
                        columnList = "genre, review_grade_avg desc"),
                @Index(name = "IX_show_review_stats__genre_state_review_grade_avg",
                        columnList = "genre, state, review_grade_avg desc"),
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShowReviewStats extends BaseTimeEntity implements Persistable<ShowId> {

    @EmbeddedId
    private ShowId id;

    @Version
    @Column(nullable = false)
    private Long version;

    @Column(name = "review_count", nullable = false)
    private Integer reviewCount = 0;

    @Column(name = "review_grade_sum", nullable = false)
    private Long reviewGradeSum = 0L;

    @Column(name = "review_grade_avg", nullable = false)
    private Double reviewGradeAvg = 0D;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", length = 25, nullable = false)
    private ShowState state;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre", length = 25, nullable = false)
    private ShowGenre genre;

    public ShowReviewStats(final ShowId id, final ShowGenre genre, final ShowState state) {
        this.id = id;
        this.genre = genre;
        this.state = state;
    }

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }

    public void applyReviewGrade(final int grade) {
        reviewCount += 1;
        reviewGradeSum += grade;
        calculateReviewGradeAvg();
    }

    public void cancelReviewGrade(final int grade) {
        if (reviewCount == 0) {
            throw new ShowReviewUnableToCancelReviewException(id);
        }
        reviewCount -= 1;
        reviewGradeSum -= grade;
        calculateReviewGradeAvg();
    }

    private void calculateReviewGradeAvg() {
        reviewGradeAvg = ((double) reviewGradeSum) / reviewCount;
    }
}
