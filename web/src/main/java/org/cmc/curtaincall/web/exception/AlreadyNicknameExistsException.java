package org.cmc.curtaincall.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

@Slf4j
public class AlreadyNicknameExistsException extends ErrorResponseException {

    private static final HttpStatus STATUS = HttpStatus.CONFLICT;

    private static final String RESPONSE_MESSAGE = "이미 존재하는 닉네임입니다.";

    public AlreadyNicknameExistsException(String message) {
        super(STATUS, ProblemDetail.forStatusAndDetail(STATUS, RESPONSE_MESSAGE), null);
        log.error(RESPONSE_MESSAGE + " " + message);
    }

}
