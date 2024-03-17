package org.cmc.curtaincall.domain.account.exception;

import org.cmc.curtaincall.domain.core.AbstractDomainException;

public final class AccountNotSignupException extends AbstractDomainException {

    public AccountNotSignupException(final String username) {
        super(AccountErrorCode.NOT_SIGNUP, "username: " + username);
    }

}
