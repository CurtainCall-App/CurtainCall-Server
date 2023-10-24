package org.cmc.curtaincall.domain.review;

import org.cmc.curtaincall.domain.review.exception.ShowReviewInvalidGradeException;
import org.cmc.curtaincall.domain.show.ShowId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ShowReviewTest {

    @Test
    void create_Success() {
        assertThatCode(() -> new ShowReview(new ShowId("show-id"), 0, "content"))
                .doesNotThrowAnyException();
        assertThatCode(() -> new ShowReview(new ShowId("show-id"), 1, "content"))
                .doesNotThrowAnyException();
        assertThatCode(() -> new ShowReview(new ShowId("show-id"), 2, "content"))
                .doesNotThrowAnyException();
        assertThatCode(() -> new ShowReview(new ShowId("show-id"), 3, "content"))
                .doesNotThrowAnyException();
        assertThatCode(() -> new ShowReview(new ShowId("show-id"), 4, "content"))
                .doesNotThrowAnyException();
        assertThatCode(() -> new ShowReview(new ShowId("show-id"), 5, "content"))
                .doesNotThrowAnyException();

    }

    @Test
    void create_GradeGreaterThan5() {
        assertThatThrownBy(() -> new ShowReview(new ShowId("show-id"), 6, "content"))
                .isInstanceOf(ShowReviewInvalidGradeException.class);
    }

    @Test
    void create_GradeLessThan0() {
        assertThatThrownBy(() -> new ShowReview(new ShowId("show-id"), -1, "content"))
                .isInstanceOf(ShowReviewInvalidGradeException.class);
    }
}