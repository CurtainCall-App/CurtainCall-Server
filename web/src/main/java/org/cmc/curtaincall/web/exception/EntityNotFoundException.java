package org.cmc.curtaincall.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

@Slf4j
public class EntityNotFoundException extends ErrorResponseException {

    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    private static final String RESPONSE_MESSAGE = "존재하지 않는 자원입니다.";

    public EntityNotFoundException(String message) {
        super(STATUS, ProblemDetail.forStatusAndDetail(STATUS, RESPONSE_MESSAGE), null);
        log.error(RESPONSE_MESSAGE + " " + message);
    }

}
