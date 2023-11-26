package org.cmc.curtaincall.domain.review.infra;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.member.dao.MemberExistsDao;
import org.cmc.curtaincall.domain.member.exception.MemberNotFoundException;
import org.cmc.curtaincall.domain.review.validation.ShowReviewMemberValidator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShowReviewMemberValidatorImpl implements ShowReviewMemberValidator {

    private final MemberExistsDao memberExistsDao;

    @Override
    public void validate(final MemberId memberId) {
        if (!memberExistsDao.exists(memberId)) {
            throw new MemberNotFoundException(memberId);
        }
    }
}
