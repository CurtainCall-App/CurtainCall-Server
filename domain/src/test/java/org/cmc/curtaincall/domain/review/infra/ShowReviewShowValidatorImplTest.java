package org.cmc.curtaincall.domain.review.infra;

import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.domain.show.dao.ShowExistsDao;
import org.cmc.curtaincall.domain.show.exception.ShowNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ShowReviewShowValidatorImplTest {

    @InjectMocks
    private ShowReviewShowValidatorImpl showReviewShowValidator;

    @Mock
    private ShowExistsDao showExistsDao;

    @Test
    void validate() {
        // given
        given(showExistsDao.exists(new ShowId("show-id"))).willReturn(true);

        // expected
        assertThatNoException().isThrownBy(() ->
                showReviewShowValidator.validate(new ShowId("show-id")));
    }

    @Test
    void validate_Fail() {
        // given
        given(showExistsDao.exists(new ShowId("show-id"))).willReturn(false);

        // expected
        assertThatThrownBy(() -> showReviewShowValidator.validate(new ShowId("show-id")))
                .isInstanceOf(ShowNotFoundException.class);
    }
}