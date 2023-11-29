package org.cmc.curtaincall.domain.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.ErrorCodeType;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCodeType {
    NOT_FOUND("MEMBER-001", "존재하지 않는 회원입니다.", 404),
    NICKNAME_ALREADY_EXISTS("MEMBER-002", "이미 존재하는 닉네임입니다.", 409),
    ;

    private final String code;

    private final String message;

    private final int status;

}
