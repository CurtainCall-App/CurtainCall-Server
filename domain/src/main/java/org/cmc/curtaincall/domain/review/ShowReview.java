package org.cmc.curtaincall.domain.review;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseTimeEntity;
import org.cmc.curtaincall.domain.show.Show;

@Entity
@Table(name = "show_review",
        indexes = {
            @Index(name = "IX_show_review__show", columnList = "show_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShowReview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "show_review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "show_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Show show;

    @Column(name = "grade", nullable = false)
    private Integer grade;

    @Column(name = "content", nullable = false)
    private String content;

    @Builder
    public ShowReview(Show show, Integer grade, String content) {
        this.show = show;
        this.grade = grade;
        this.content = content;
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
}
