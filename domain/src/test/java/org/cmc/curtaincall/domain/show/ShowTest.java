package org.cmc.curtaincall.domain.show;

import org.cmc.curtaincall.domain.show.exception.ShowUnableToCancelReviewException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ShowTest {

    @Test
    void applyReviewGrade() {
        // given
        Show show = Show.builder()
                .id("show-id")
                .facility(Facility.builder().build())
                .name("name")
                .startDate(LocalDate.of(2023, 10, 24))
                .endDate(LocalDate.of(2023, 10, 24))
                .crew("crew")
                .cast("cast")
                .runtime("runtime")
                .enterprise("enterprise")
                .ticketPrice("ticket")
                .poster("poster")
                .story("story")
                .genre(ShowGenre.MUSICAL)
                .state(ShowState.PERFORMING)
                .openRun("r")
                .build();

        // when
        show.applyReviewGrade(5);
        show.applyReviewGrade(4);
        show.applyReviewGrade(3);

        // then
        assertThat(show.getReviewCount()).isEqualTo(3);
        assertThat(show.getReviewGradeSum()).isEqualTo(12);
        assertThat(show.getReviewGradeAvg()).isEqualTo(4.0);
    }

    @Test
    void cancelReviewGrade() {
        // given
        Show show = Show.builder()
                .id("show-id")
                .facility(Facility.builder().build())
                .name("name")
                .startDate(LocalDate.of(2023, 10, 24))
                .endDate(LocalDate.of(2023, 10, 24))
                .crew("crew")
                .cast("cast")
                .runtime("runtime")
                .enterprise("enterprise")
                .ticketPrice("ticket")
                .poster("poster")
                .story("story")
                .genre(ShowGenre.MUSICAL)
                .state(ShowState.PERFORMING)
                .openRun("r")
                .build();

        // when
        show.applyReviewGrade(5);
        show.applyReviewGrade(4);
        show.applyReviewGrade(3);

        show.cancelReviewGrade(3);

        // then
        assertThat(show.getReviewCount()).isEqualTo(2);
        assertThat(show.getReviewGradeSum()).isEqualTo(9);
        assertThat(show.getReviewGradeAvg()).isEqualTo(4.5);
    }

    @Test
    void cancelReviewGrade_ShowUnableToCancelReviewException() {
        // given
        Show show = Show.builder()
                .id("show-id")
                .facility(Facility.builder().build())
                .name("name")
                .startDate(LocalDate.of(2023, 10, 24))
                .endDate(LocalDate.of(2023, 10, 24))
                .crew("crew")
                .cast("cast")
                .runtime("runtime")
                .enterprise("enterprise")
                .ticketPrice("ticket")
                .poster("poster")
                .story("story")
                .genre(ShowGenre.MUSICAL)
                .state(ShowState.PERFORMING)
                .openRun("r")
                .build();

        // expected
        assertThatThrownBy(() -> show.cancelReviewGrade(3))
                .isInstanceOf(ShowUnableToCancelReviewException.class);
    }
}