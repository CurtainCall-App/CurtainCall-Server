package org.cmc.curtaincall.domain.review.infra;

import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.review.ShowReviewId;
import org.cmc.curtaincall.domain.review.dao.ShowReviewExistsDao;
import org.cmc.curtaincall.domain.review.exception.ShowReviewAccessDeniedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ShowReviewCreatorValidatorImplTest {

    @InjectMocks
    private ShowReviewCreatorValidatorImpl showReviewCreatorValidator;

    @Mock
    private ShowReviewExistsDao showReviewExistsDao;

    @Test
    void validate() {
        // given
        given(showReviewExistsDao.existsByIdAndCreatedBy(
                new ShowReviewId(10L), new CreatorId(new MemberId(20L)))
        ).willReturn(true);

        // expected
        assertThatNoException()
                .isThrownBy(() -> showReviewCreatorValidator.validate(
                        new ShowReviewId(10L), new CreatorId(new MemberId(20L)))
                );

    }

    @Test
    void validate_Fail() {
        // given
        given(showReviewExistsDao.existsByIdAndCreatedBy(
                new ShowReviewId(10L), new CreatorId(new MemberId(20L)))
        ).willReturn(false);

        // expected
        assertThatThrownBy(() -> showReviewCreatorValidator.validate(
                new ShowReviewId(10L), new CreatorId(new MemberId(20L)))
        ).isInstanceOf(ShowReviewAccessDeniedException.class);
    }
}