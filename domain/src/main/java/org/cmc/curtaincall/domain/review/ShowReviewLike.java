package org.cmc.curtaincall.domain.review;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.member.Member;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    public ShowReviewLike(ShowReview showReview, Member member) {
        this.showReview = showReview;
        this.member = member;
    }
}
