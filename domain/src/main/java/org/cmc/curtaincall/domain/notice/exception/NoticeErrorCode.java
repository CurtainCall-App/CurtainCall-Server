package org.cmc.curtaincall.domain.notice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.ErrorCodeType;

@Getter
@RequiredArgsConstructor
public enum NoticeErrorCode implements ErrorCodeType {

    NOT_FOUND("NOTICE-001", "존재하지 않는 공지입니다.", 404),
    ;

    private final String code;

    private final String message;

    private final int status;
}
