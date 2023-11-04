package org.cmc.curtaincall.domain.review.validation;

import org.cmc.curtaincall.domain.member.MemberId;

public interface ShowReviewMemberValidator {

    void validate(final MemberId memberId);
}
