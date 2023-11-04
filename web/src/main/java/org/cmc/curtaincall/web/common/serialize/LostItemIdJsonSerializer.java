package org.cmc.curtaincall.web.common.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.cmc.curtaincall.domain.lostitem.LostItemId;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class LostItemIdJsonSerializer extends JsonSerializer<LostItemId> {

    @Override
    public void serialize(LostItemId value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(value.getId());
    }
}
