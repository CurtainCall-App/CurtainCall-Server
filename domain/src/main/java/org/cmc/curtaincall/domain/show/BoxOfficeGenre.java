package org.cmc.curtaincall.domain.show;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.EnumMapperType;

@Getter
@RequiredArgsConstructor
public enum BoxOfficeGenre implements EnumMapperType {

    PLAY("AAAA", "연극"),
    MUSICAL("GGGA", "뮤지컬"),
    ALL(null, "전체"),
    ;

    private final String code;

    private final String title;
}
