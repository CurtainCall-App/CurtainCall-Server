package org.cmc.curtaincall.domain.lostitem.exception;

import org.cmc.curtaincall.domain.core.AbstractDomainException;
import org.cmc.curtaincall.domain.lostitem.LostItemId;

public class LostItemNotFoundException extends AbstractDomainException {

    public LostItemNotFoundException(final LostItemId lostItemId) {
        super(LostItemErrorCode.NOT_FOUND, "LostItem.id=" + lostItemId);
    }

}
