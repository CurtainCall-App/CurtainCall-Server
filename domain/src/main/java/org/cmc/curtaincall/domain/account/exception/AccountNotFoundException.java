package org.cmc.curtaincall.domain.account.exception;

import org.cmc.curtaincall.domain.core.AbstractDomainException;

public final class AccountNotFoundException extends AbstractDomainException {

    public AccountNotFoundException(String message) {
        super(AccountErrorCode.NOT_FOUND, message);
    }

}
