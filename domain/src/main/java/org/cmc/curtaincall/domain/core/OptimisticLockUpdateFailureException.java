package org.cmc.curtaincall.domain.core;

public class OptimisticLockUpdateFailureException extends AbstractDomainException {

    public OptimisticLockUpdateFailureException(final Throwable cause) {
        super(CommonErrorCode.OPTIMISTIC_LOCK_UPDATE_FAILURE, cause);
    }
}
