package org.cmc.curtaincall.domain.party.convert;

import org.cmc.curtaincall.domain.party.PartyId;
import org.springframework.core.convert.converter.Converter;

public class StringToPartyIdConverter implements Converter<String, PartyId> {

    @Override
    public PartyId convert(String source) {
        return new PartyId(Long.parseLong(source));
    }
}
