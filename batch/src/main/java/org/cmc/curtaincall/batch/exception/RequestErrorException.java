package org.cmc.curtaincall.batch.exception;

public class RequestErrorException extends RuntimeException {

    public RequestErrorException(Throwable cause) {
        super(cause);
    }

    public RequestErrorException(String message) {
        super(message);
    }

    public RequestErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}