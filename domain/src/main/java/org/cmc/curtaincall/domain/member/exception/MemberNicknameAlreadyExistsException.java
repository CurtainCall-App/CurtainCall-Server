package org.cmc.curtaincall.domain.member.exception;

import org.cmc.curtaincall.domain.common.DomainErrorCode;
import org.cmc.curtaincall.domain.common.DomainException;

public class MemberNicknameAlreadyExistsException extends DomainException {

    public MemberNicknameAlreadyExistsException(final String nickname) {
        super(DomainErrorCode.CONFLICT, "nickname=" + nickname);
    }

    @Override
    public String getExternalMessage() {
        return "이미 존재하는 닉네임입니다.";
    }
}
