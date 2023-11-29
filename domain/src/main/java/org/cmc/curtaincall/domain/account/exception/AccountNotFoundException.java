package org.cmc.curtaincall.domain.account.exception;

import org.cmc.curtaincall.domain.core.DomainErrorCode;
import org.cmc.curtaincall.domain.core.DomainException;

public final class AccountNotFoundException extends DomainException {

    public AccountNotFoundException(String message) {
        super(DomainErrorCode.NOT_FOUND, message);
    }

    @Override
    public String getExternalMessage() {
        return "존재하지 않는 계정입니다.";
    }
}
