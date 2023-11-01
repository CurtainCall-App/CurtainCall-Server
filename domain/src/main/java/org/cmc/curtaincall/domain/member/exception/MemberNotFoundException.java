package org.cmc.curtaincall.domain.member.exception;

import org.cmc.curtaincall.domain.common.DomainErrorCode;
import org.cmc.curtaincall.domain.common.DomainException;
import org.cmc.curtaincall.domain.member.MemberId;

public class MemberNotFoundException extends DomainException {

    public MemberNotFoundException(final MemberId memberId) {
        super(DomainErrorCode.NOT_FOUND, memberId.toString());
    }

    @Override
    public String getExternalMessage() {
        return "존재하지 않는 회원입니다.";
    }
}
