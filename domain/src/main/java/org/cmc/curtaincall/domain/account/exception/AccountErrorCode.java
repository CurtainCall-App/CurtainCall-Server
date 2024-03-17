package org.cmc.curtaincall.domain.account.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.ErrorCodeType;

@Getter
@RequiredArgsConstructor
public enum AccountErrorCode implements ErrorCodeType {

    NOT_FOUND("ACCOUNT-001", "존재하지 않는 계정입니다.", 404),
    ALREADY_SIGNUP("ACCOUNT-002", "이미 가입된 계정입니다.", 409),
    NOT_SIGNUP("ACCOUNT-003", "가입되지 않은 계정입니다.", 400),
    ;

    private final String code;

    private final String message;

    private final int status;
}
