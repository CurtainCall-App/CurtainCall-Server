package org.cmc.curtaincall.domain.show;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.EnumMapperType;

@RequiredArgsConstructor
public enum ShowDay implements EnumMapperType {

    MONDAY("월요일"),
    TUESDAY("화요일"),
    WEDNESDAY("수요일"),
    THURSDAY("목요일"),
    FRIDAY("금요일"),
    SATURDAY("토요일"),
    SUNDAY("일요일"),
    HOL("HOL"),
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
