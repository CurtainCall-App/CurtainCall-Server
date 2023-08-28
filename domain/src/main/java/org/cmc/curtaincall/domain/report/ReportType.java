package org.cmc.curtaincall.domain.report;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.EnumMapperType;

@Getter
@RequiredArgsConstructor
public enum ReportType implements EnumMapperType {
    PARTY("파티"),
    SHOW_REVIEW("리뷰"),
    LOST_ITEM("분실물")
    ;

    private final String title;


    @Override
    public String getCode() {
        return name();
    }
}
