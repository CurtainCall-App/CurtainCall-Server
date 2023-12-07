package org.cmc.curtaincall.domain.review;

import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.review.exception.ShowReviewInvalidGradeException;
import org.cmc.curtaincall.domain.show.ShowId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ShowReviewTest {

    @Test
    void create_Success() {
        assertThatCode(() -> new ShowReview(new ShowId("show-id"), 0, "content", new CreatorId(1L)))
                .doesNotThrowAnyException();
        assertThatCode(() -> new ShowReview(new ShowId("show-id"), 1, "content", new CreatorId(1L)))
                .doesNotThrowAnyException();
        assertThatCode(() -> new ShowReview(new ShowId("show-id"), 2, "content", new CreatorId(1L)))
                .doesNotThrowAnyException();
        assertThatCode(() -> new ShowReview(new ShowId("show-id"), 3, "content", new CreatorId(1L)))
                .doesNotThrowAnyException();
        assertThatCode(() -> new ShowReview(new ShowId("show-id"), 4, "content", new CreatorId(1L)))
                .doesNotThrowAnyException();
        assertThatCode(() -> new ShowReview(new ShowId("show-id"), 5, "content", new CreatorId(1L)))
                .doesNotThrowAnyException();

    }

    @Test
    void create_GradeGreaterThan5() {
        assertThatThrownBy(() -> new ShowReview(new ShowId("show-id"), 6, "content", new CreatorId(1L)))
                .isInstanceOf(ShowReviewInvalidGradeException.class);
    }

    @Test
    void create_GradeLessThan0() {
        assertThatThrownBy(() -> new ShowReview(new ShowId("show-id"), -1, "content", new CreatorId(1L)))
                .isInstanceOf(ShowReviewInvalidGradeException.class);
    }
}