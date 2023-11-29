package org.cmc.curtaincall.domain.lostitem.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.ErrorCodeType;

@Getter
@RequiredArgsConstructor
public enum LostItemErrorCode implements ErrorCodeType {
    NOT_FOUND("LOSTITEM-001", "존재하지 않는 분실물입니다.", 404),
    ACCESS_DENIED("LOSTITEM-002", "작성자에게만 허용된 기능입니다.", 403),
    ;

    private final String code;

    private final String message;

    private final int status;
}
