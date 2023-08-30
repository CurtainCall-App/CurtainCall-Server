package org.cmc.curtaincall.web.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalApiExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler({
            MaxUploadSizeExceededException.class,
            AccessDeniedException.class,
            OptimisticLockingFailureException.class,
            Exception.class
    })
    public final ResponseEntity<Object> handleUnhandledException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        if (ex instanceof MaxUploadSizeExceededException subEx) {
            return handleMaxUploadSizeExceededException(subEx, headers, HttpStatus.FORBIDDEN, request);
        } else if (ex instanceof AccessDeniedException subEx) {
            return handleAccessDeniedException(subEx, headers, HttpStatus.FORBIDDEN, request);
        } else if (ex instanceof OptimisticLockingFailureException subEx) {
            return handleOptimisticLockingFailureException(subEx, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
        } else {
            return handleRootException(ex, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
    }

    /**
     * 파일 업로드 용량 초과시 발생
     */
    protected ResponseEntity<Object> handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail body = createProblemDetail(
                ex, status, "이미지 용량이 초과되었습니다.", null, null, request);
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    protected ResponseEntity<Object> handleAccessDeniedException(
            AccessDeniedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail body = createProblemDetail(
                ex, status, "접근이 허용되지 않습니다.", null, null, request);
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    protected ResponseEntity<Object> handleOptimisticLockingFailureException(
            OptimisticLockingFailureException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail body = createProblemDetail(
                ex, status, "업데이트에 실패했습니다. 다시 시도해주세요",
                null, null, request);
        return handleExceptionInternal(ex, body, headers, status, request);
    }


    protected ResponseEntity<Object> handleRootException(
            Exception ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail body = createProblemDetail(
                ex, status, "서버에 문제가 발생했습니다.", null, null, request);
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        log.error("handleExceptionInternal", ex);
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }
}
