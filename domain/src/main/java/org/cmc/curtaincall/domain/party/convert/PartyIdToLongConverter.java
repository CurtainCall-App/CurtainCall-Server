package org.cmc.curtaincall.domain.party.convert;

import org.cmc.curtaincall.domain.party.PartyId;
import org.springframework.core.convert.converter.Converter;

public class PartyIdToLongConverter implements Converter<PartyId, Long> {

    @Override
    public Long convert(PartyId source) {
        return source.getId();
    }
}
