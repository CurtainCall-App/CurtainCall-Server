package org.cmc.curtaincall.domain.review;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseEntity;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.review.exception.ShowReviewInvalidGradeException;
import org.cmc.curtaincall.domain.review.repository.ShowReviewLikeRepository;
import org.cmc.curtaincall.domain.show.ShowId;

import java.util.stream.IntStream;

@Entity
@Table(name = "show_review",
        indexes = {
                @Index(name = "IX_show_review__created_by_created_at",
                        columnList = "created_by, created_at desc"),
                @Index(name = "IX_show_review__show_created_by_created_at",
                        columnList = "show_id, created_by, created_at desc"),
                @Index(name = "IX_show_review__show_like_count_created_at",
                        columnList = "show_id, like_count desc, created_at desc"),
                @Index(name = "IX_show_review__show_created_at",
                        columnList = "show_id, created_at desc")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShowReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "show_review_id")
    private Long id;

    @Version
    @Column(nullable = false)
    private Long version;

    @Embedded
    private ShowId showId;

    @Column(name = "grade", nullable = false)
    private Integer grade;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "like_count", nullable = false)
    private Integer likeCount;

    @Builder
    public ShowReview(final ShowId showId, final int grade, final String content) {
        if (isInGradeRange(grade)) {
            throw new ShowReviewInvalidGradeException(grade);
        }
        this.showId = showId;
        this.grade = grade;
        this.content = content;
        this.likeCount = 0;
    }

    private boolean isInGradeRange(int grade) {
        return IntStream.rangeClosed(0, 5).noneMatch(i -> i == grade);
    }

    public ShowReviewEditor.ShowReviewEditorBuilder toEditor() {
        return ShowReviewEditor.builder()
                .grade(grade)
                .content(content);
    }

    public void edit(final ShowReviewEditor editor) {
        grade = editor.getGrade();
        content = editor.getContent();
    }

    public void like(final MemberId memberId, final ShowReviewLikeRepository showReviewLikeRepository) {
        showReviewLikeRepository.save(new ShowReviewLike(this, memberId));
        plusLikeCount();
    }

    private void plusLikeCount() {
        this.likeCount += 1;
    }

    public void cancelLike(final MemberId memberId, final ShowReviewLikeRepository showReviewLikeRepository) {
        showReviewLikeRepository.findByMemberIdAndShowReview(memberId, this)
                .ifPresent(showReviewLikeRepository::delete);
        minusLikeCount();
    }

    private void minusLikeCount() {
        if (this.likeCount == 0) {
            throw new IllegalStateException("좋아요 개수는 음수가 될 수 없음");
        }
        this.likeCount -= 1;
    }
}
