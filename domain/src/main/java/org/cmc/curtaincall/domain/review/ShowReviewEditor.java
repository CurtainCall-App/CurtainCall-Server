package org.cmc.curtaincall.domain.review;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ShowReviewEditor {

    private Integer grade;

    private String content;

    @Builder
    public ShowReviewEditor(Integer grade, String content) {
        this.grade = grade;
        this.content = content;
    }
}
