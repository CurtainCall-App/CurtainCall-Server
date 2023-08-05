package org.cmc.curtaincall.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

@Slf4j
public class AlreadySignupAccountException extends ErrorResponseException {

    private static final HttpStatus STATUS = HttpStatus.CONFLICT;

    private static final String RESPONSE_MESSAGE = "이미 가입한 계정입니다.";

    public AlreadySignupAccountException(String message) {
        super(STATUS, ProblemDetail.forStatusAndDetail(STATUS, RESPONSE_MESSAGE), null);
        log.error(RESPONSE_MESSAGE + " " + message);
    }

}
