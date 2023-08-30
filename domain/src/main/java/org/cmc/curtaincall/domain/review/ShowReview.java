package org.cmc.curtaincall.domain.review;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseEntity;
import org.cmc.curtaincall.domain.show.Show;

@Entity
@Table(name = "show_review",
        indexes = {
                @Index(name = "IX_show_review__show_created_by_created_at",
                        columnList = "show_id, created_by, created_at desc"),
                @Index(name = "IX_show_review__show_like_count_created_at",
                        columnList = "show_id, like_count desc, created_at desc"),
                @Index(name = "IX_show_review__show_created_at",
                        columnList = "show_id, like_count desc, created_at desc")
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "show_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Show show;

    @Column(name = "grade", nullable = false)
    private Integer grade;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "like_count", nullable = false)
    private Integer likeCount;

    @Builder
    public ShowReview(Show show, Integer grade, String content) {
        this.show = show;
        this.grade = grade;
        this.content = content;
        this.likeCount = 0;
    }

    public ShowReviewEditor.ShowReviewEditorBuilder toEditor() {
        return ShowReviewEditor.builder()
                .grade(grade)
                .content(content);
    }

    public void edit(ShowReviewEditor editor) {
        grade = editor.getGrade();
        content = editor.getContent();
    }

    public void plusLikeCount() {
        this.likeCount += 1;
    }

    public void minusLikeCount() {
        this.likeCount -= 1;
    }
}
