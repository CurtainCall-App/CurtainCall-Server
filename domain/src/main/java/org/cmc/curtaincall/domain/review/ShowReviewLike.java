package org.cmc.curtaincall.domain.review;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.member.MemberId;

@Entity
@Table(name = "show_review_like",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "UK_show_review_like__member_show_review",
                    columnNames = {"member_id", "show_review_id"}
            )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShowReviewLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "show_review_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "show_review_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ShowReview showReview;

    @Embedded
    private MemberId memberId;

    public ShowReviewLike(final ShowReview showReview, final MemberId memberId) {
        this.showReview = showReview;
        this.memberId = memberId;
    }
}
