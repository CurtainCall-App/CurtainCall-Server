package org.cmc.curtaincall.domain.core;

import lombok.Getter;

@Getter
public abstract class DomainException extends RuntimeException {

    protected final DomainErrorCode code;

    protected DomainException(DomainErrorCode code) {
        super();
        this.code = code;
    }

    protected DomainException(DomainErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    protected DomainException(DomainErrorCode code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    protected DomainException(DomainErrorCode code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public abstract String getExternalMessage();
}
