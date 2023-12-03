package org.cmc.curtaincall.domain.account.exception;

import org.cmc.curtaincall.domain.core.AbstractDomainException;

public class AccountAlreadySignupException extends AbstractDomainException {

    public AccountAlreadySignupException(final String username) {
        super(AccountErrorCode.ALREADY_SIGNUP, "username=" + username);
    }
}
