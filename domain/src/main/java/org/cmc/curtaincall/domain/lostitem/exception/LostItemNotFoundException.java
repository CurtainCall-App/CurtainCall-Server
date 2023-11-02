package org.cmc.curtaincall.domain.lostitem.exception;

import org.cmc.curtaincall.domain.common.DomainErrorCode;
import org.cmc.curtaincall.domain.common.DomainException;
import org.cmc.curtaincall.domain.lostitem.LostItemId;

public class LostItemNotFoundException extends DomainException {

    public LostItemNotFoundException(final LostItemId lostItemId) {
        super(DomainErrorCode.NOT_FOUND, "LostItemId=" + lostItemId);
    }

    @Override
    public String getExternalMessage() {
        return "존재하지 않는 분실물입니다.";
    }
}
