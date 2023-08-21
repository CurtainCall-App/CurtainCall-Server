package org.cmc.curtaincall.web.code;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.cmc.curtaincall.domain.core.EnumMapperType;

@Getter
@ToString
@EqualsAndHashCode
public class EnumMapperValue {

    private final String code;
    private final String title;

    public EnumMapperValue(EnumMapperType enumMapperType) {
        this.code = enumMapperType.getCode();
        this.title = enumMapperType.getTitle();
    }
}
