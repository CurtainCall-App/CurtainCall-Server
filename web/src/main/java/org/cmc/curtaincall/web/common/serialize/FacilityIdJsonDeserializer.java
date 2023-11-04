package org.cmc.curtaincall.web.common.serialize;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.cmc.curtaincall.domain.show.FacilityId;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class FacilityIdJsonDeserializer extends JsonDeserializer<FacilityId> {

    @Override
    public FacilityId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        return new FacilityId(p.getText());
    }
}
