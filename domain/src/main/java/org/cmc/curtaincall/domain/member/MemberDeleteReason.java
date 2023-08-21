package org.cmc.curtaincall.domain.member;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.EnumMapperType;

@RequiredArgsConstructor
public enum MemberDeleteReason implements EnumMapperType {
    RECORD_DELETION("기록을 삭제하기 위해"),
    INCONVENIENCE_FREQUENT_ERROR("이용이 불편하고 장애가 잦아서"),
    BETTER_OTHER_SERVICE("타 서비스가 더 좋아서"),
    LOW_USAGE_FREQUENCY("사용빈도가 낮아서"),
    NOT_USEFUL("앱 기능이 유용하지 않아서"),
    ETC("기타"),
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
