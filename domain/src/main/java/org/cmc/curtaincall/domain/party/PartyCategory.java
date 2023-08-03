package org.cmc.curtaincall.domain.party;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.EnumMapperType;

@RequiredArgsConstructor
public enum PartyCategory implements EnumMapperType {

    WATCHING("공연 관람"),
    FOOD_CAFE("식사/카페"),
    ETC("기타")
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
