package org.cmc.curtaincall.domain.member.exception;

import org.cmc.curtaincall.domain.core.AbstractDomainException;

public class MemberNicknameAlreadyExistsException extends AbstractDomainException {

    public MemberNicknameAlreadyExistsException(final String nickname) {
        super(MemberErrorCode.NICKNAME_ALREADY_EXISTS, "nickname=" + nickname);
    }

}
