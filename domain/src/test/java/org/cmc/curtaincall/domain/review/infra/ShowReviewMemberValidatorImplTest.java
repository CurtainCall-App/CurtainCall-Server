package org.cmc.curtaincall.domain.review.infra;

import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.member.dao.MemberExistsDao;
import org.cmc.curtaincall.domain.member.exception.MemberNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ShowReviewMemberValidatorImplTest {

    @InjectMocks
    private ShowReviewMemberValidatorImpl showReviewMemberValidator;

    @Mock
    private MemberExistsDao memberExistsDao;

    @Test
    void validate() {
        // given
        given(memberExistsDao.exists(new MemberId(10L)))
                .willReturn(true);

        // expected
        assertThatNoException()
                .isThrownBy(() -> showReviewMemberValidator.validate(new MemberId(10L)));
    }

    @Test
    void validate_Fail() {
        // given
        given(memberExistsDao.exists(new MemberId(10L)))
                .willReturn(false);

        // expected
        assertThatThrownBy(() -> showReviewMemberValidator.validate(new MemberId(10L)))
                .isInstanceOf(MemberNotFoundException.class);
    }
}