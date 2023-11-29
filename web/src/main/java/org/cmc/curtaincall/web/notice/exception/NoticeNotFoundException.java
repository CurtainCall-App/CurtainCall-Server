package org.cmc.curtaincall.web.notice.exception;

import org.cmc.curtaincall.domain.core.DomainErrorCode;
import org.cmc.curtaincall.domain.core.DomainException;
import org.cmc.curtaincall.domain.notice.NoticeId;

public class NoticeNotFoundException extends DomainException {

    public NoticeNotFoundException(NoticeId noticeId) {
        super(DomainErrorCode.NOT_FOUND, "Notice.id=" + noticeId);
    }

    @Override
    public String getExternalMessage() {
        return "공지사항이 존재하지 않음";
    }
}
