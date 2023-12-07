package org.cmc.curtaincall.domain.notice.exception;

import org.cmc.curtaincall.domain.core.AbstractDomainException;
import org.cmc.curtaincall.domain.notice.NoticeId;

public class NoticeNotFoundException extends AbstractDomainException {

    public NoticeNotFoundException(final NoticeId noticeId) {
        super(NoticeErrorCode.NOT_FOUND, "NoticeId=" + noticeId);
    }
}
