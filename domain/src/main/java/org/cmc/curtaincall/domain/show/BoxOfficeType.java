package org.cmc.curtaincall.domain.show;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.EnumMapperType;

@RequiredArgsConstructor
public enum BoxOfficeType implements EnumMapperType {
    DAY("일"),
    WEEK("주"),
    MONTH("월")
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
