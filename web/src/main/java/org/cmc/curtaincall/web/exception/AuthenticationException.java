package org.cmc.curtaincall.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

@Slf4j
public class AuthenticationException extends ErrorResponseException {

    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    private static final String RESPONSE_MESSAGE = "인증되지 않은 사용자입니다.";

    public AuthenticationException(String message) {
        this(message, null);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(STATUS, ProblemDetail.forStatusAndDetail(STATUS, RESPONSE_MESSAGE), cause);
        log.error(RESPONSE_MESSAGE + " " + message);
    }
}
