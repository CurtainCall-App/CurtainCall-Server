package org.cmc.curtaincall.domain.review;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseTimeEntity;

@Entity
@Table(name = "show_review",
        indexes = {
            @Index(name = "IX_show_review__mt20id", columnList = "mt20id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShowReview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "show_review_id")
    private Long id;

    @Column(name = "mt20id", length = 25, nullable = false)
    private String showId;

    @Column(name = "grade", nullable = false)
    private Integer grade;

    @Column(name = "content", nullable = false)
    private String content;

    @Builder
    public ShowReview(String showId, Integer grade, String content) {
        this.showId = showId;
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
