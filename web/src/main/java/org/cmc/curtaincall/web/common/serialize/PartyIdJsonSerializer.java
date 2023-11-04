package org.cmc.curtaincall.web.common.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.cmc.curtaincall.domain.party.PartyId;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class PartyIdJsonSerializer extends JsonSerializer<PartyId> {

    @Override
    public void serialize(PartyId value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(value.getId());
    }
}
