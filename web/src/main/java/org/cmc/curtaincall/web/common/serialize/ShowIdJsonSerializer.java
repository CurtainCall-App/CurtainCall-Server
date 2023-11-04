package org.cmc.curtaincall.web.common.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.cmc.curtaincall.domain.show.ShowId;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class ShowIdJsonSerializer extends JsonSerializer<ShowId> {

    @Override
    public void serialize(ShowId value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.getId());
    }
}
