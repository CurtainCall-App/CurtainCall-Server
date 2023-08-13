package org.cmc.curtaincall.domain.report;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.EnumMapperType;

@Getter
@RequiredArgsConstructor
public enum ReportReason implements EnumMapperType {

    SPAM("스팸홍보/도배글입니다."),
    HATE_SPEECH("욕설/혐오/차별적 표현입니다."),
    ILLEGAL("불법정보를 포함하고 있습니다."),
    BAD_MANNERS("비매너 사용자입니다."),
    HARMFUL_TO_TEENAGER("청소년에게 유해한 내용입니다."),
    PERSONAL_INFORMATION_DISCLOSURE("개인정보 노출 게시물입니다."),
    ETC("다른 문제가 있습니다."),
    ;

    private final String title;

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getTitle() {
        return title;
    }
}
