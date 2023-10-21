package org.cmc.curtaincall.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DomainErrorCode {

    BAD_REQUEST(400),
    FORBIDDEN(403),
    NOT_FOUND(404),
    CONFLICT(409),
    INTERNAL_SERVER_ERROR(500)
    ;

    private final int status;
}
