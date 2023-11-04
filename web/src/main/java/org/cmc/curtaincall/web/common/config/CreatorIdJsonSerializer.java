package org.cmc.curtaincall.web.common.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class CreatorIdJsonSerializer extends JsonSerializer<CreatorId> {

    @Override
    public void serialize(CreatorId value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(value.getId());
    }
}
