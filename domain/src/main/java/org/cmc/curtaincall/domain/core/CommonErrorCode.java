package org.cmc.curtaincall.domain.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
enum CommonErrorCode implements ErrorCodeType {

    OPTIMISTIC_LOCK_UPDATE_FAILURE("COMMON-001", "업데이트에 실패했습니다. 잠시후 다시 시도해주세요.", 500),
    ;

    private final String code;

    private final String message;

    private final int status;

}
