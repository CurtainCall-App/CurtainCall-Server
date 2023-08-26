package org.cmc.curtaincall.domain.show;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.EnumMapperType;

@RequiredArgsConstructor
public enum ShowState implements EnumMapperType {
    TO_PERFORM("공연예정"),
    PERFORMING("공연중"),
    COMPLETE("공연완료"),
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
