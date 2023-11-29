package org.cmc.curtaincall.domain.party.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.ErrorCodeType;

@Getter
@RequiredArgsConstructor
public enum PartyErrorCode implements ErrorCodeType {
    NOT_FOUND("PARTY-001", "존재하지 않는 파티 입니다.", 404),
    ACCESS_DENIED("PARTY-002", "작성자에게만 허용된 기능입니다.", 403),
    ALREADY_CLOSED("PARTY-003", "마감된 파티입니다.", 400),
    ALREADY_PARTICIPATED("PARTY-004", "이미 참여한 파티입니다.", 409),
    ;

    private final String code;

    private final String message;

    private final int status;

}
