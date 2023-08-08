package org.cmc.curtaincall.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

@Slf4j
public class EntityAccessDeniedException extends ErrorResponseException {

    private static final HttpStatus STATUS = HttpStatus.FORBIDDEN;

    private static final String RESPONSE_MESSAGE = "접근이 허용되지 않는 자원입니다.";

    public EntityAccessDeniedException(String message) {
        super(STATUS, ProblemDetail.forStatusAndDetail(STATUS, RESPONSE_MESSAGE), null);
        log.error(RESPONSE_MESSAGE + " " + message);
    }

}
