package org.cmc.curtaincall.domain.review;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseCreatedByEntity;

@Entity
@Table(name = "show_review_like",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "UK_show_review_like__created_by_show_review",
                    columnNames = {"created_by", "show_review_id"}
            )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShowReviewLike extends BaseCreatedByEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "show_review_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "show_review_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ShowReview showReview;

    @Builder
    public ShowReviewLike(ShowReview showReview) {
        this.showReview = showReview;
    }
}
