package org.cmc.curtaincall.domain.show.convert;

import org.cmc.curtaincall.domain.show.ShowId;
import org.springframework.core.convert.converter.Converter;

public class StringToShowIdConverter implements Converter<String, ShowId> {

    @Override
    public ShowId convert(String source) {
        return new ShowId(source);
    }
}
