package org.cmc.curtaincall.domain.lostitem.convert;

import org.cmc.curtaincall.domain.lostitem.LostItemId;
import org.springframework.core.convert.converter.Converter;

public class StringToLostItemIdConverter implements Converter<String, LostItemId> {

    @Override
    public LostItemId convert(String source) {
        return new LostItemId(Long.parseLong(source));
    }
}
