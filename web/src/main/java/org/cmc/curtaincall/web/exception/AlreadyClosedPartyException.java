package org.cmc.curtaincall.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

@Slf4j
public class AlreadyClosedPartyException extends ErrorResponseException {

    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    private static final String RESPONSE_MESSAGE = "이미 마감된 파티입니다.";

    public AlreadyClosedPartyException(String message) {
        super(STATUS, ProblemDetail.forStatusAndDetail(STATUS, RESPONSE_MESSAGE), null);
        log.error(RESPONSE_MESSAGE + " " + message);
    }

}
