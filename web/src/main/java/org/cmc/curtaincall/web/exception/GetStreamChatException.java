package org.cmc.curtaincall.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

@Slf4j
public class GetStreamChatException extends ErrorResponseException {

    private static final HttpStatus STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    private static final String RESPONSE_MESSAGE = "채팅 요청에 실패했습니다.";

    public GetStreamChatException(Throwable cause) {
        super(STATUS, ProblemDetail.forStatusAndDetail(STATUS, RESPONSE_MESSAGE), cause);
        log.error(RESPONSE_MESSAGE + " " + cause);
    }

}
