package org.cmc.curtaincall.web.common.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.cmc.curtaincall.domain.show.FacilityId;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class FacilityIdJsonSerializer extends JsonSerializer<FacilityId> {

    @Override
    public void serialize(FacilityId value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.getId());
    }
}
