package org.cmc.curtaincall.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

@Slf4j
public class InvalidImageException extends ErrorResponseException {

    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    private static final String RESPONSE_MESSAGE = "잘못된 이미지를 업로드하였습니다.";

    public InvalidImageException(String message) {
        super(STATUS, ProblemDetail.forStatusAndDetail(STATUS, RESPONSE_MESSAGE), null);
        log.error(RESPONSE_MESSAGE + " " + message);
    }

    public InvalidImageException(Throwable cause) {
        super(STATUS, ProblemDetail.forStatusAndDetail(STATUS, RESPONSE_MESSAGE), cause);
        log.error(RESPONSE_MESSAGE + " " + cause);
    }

}
