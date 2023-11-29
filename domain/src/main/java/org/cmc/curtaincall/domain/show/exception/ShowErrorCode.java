package org.cmc.curtaincall.domain.show.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.ErrorCodeType;

@Getter
@RequiredArgsConstructor
public enum ShowErrorCode implements ErrorCodeType {
    NOT_FOUND("SHOW-001", "존재하지 않는 공연입니다.", 404),
    FACILITY_NOT_FOUND("SHOW-002", "존재하지 않는 공연장입니다.", 404),
    ;

    private final String code;

    private final String message;

    private final int status;

}
