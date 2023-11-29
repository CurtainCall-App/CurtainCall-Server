package org.cmc.curtaincall.domain.member.exception;

import org.cmc.curtaincall.domain.core.AbstractDomainException;
import org.cmc.curtaincall.domain.member.MemberId;

public class MemberNotFoundException extends AbstractDomainException {

    public MemberNotFoundException(final MemberId memberId) {
        super(MemberErrorCode.NOT_FOUND, "memberId=" + memberId);
    }
}
