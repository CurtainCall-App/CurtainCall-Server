package org.cmc.curtaincall.domain.lostitem;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.EnumMapperType;

@RequiredArgsConstructor
public enum LostItemType implements EnumMapperType {

    BAG("가방"),
    WALLET("지갑"),
    CASH("현금"),
    CARD("카드"),
    JEWELRY("귀금속"),
    ELECTRONIC_EQUIPMENT("전자기기"),
    BOOK("책"),
    CLOTHING("의류"),
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
