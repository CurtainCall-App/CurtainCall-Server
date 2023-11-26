package org.cmc.curtaincall.domain.show.convert;

import org.cmc.curtaincall.domain.show.FacilityId;
import org.springframework.core.convert.converter.Converter;

public class StringToFacilityIdConverter implements Converter<String, FacilityId> {

    @Override
    public FacilityId convert(String source) {
        return new FacilityId(source);
    }
}
