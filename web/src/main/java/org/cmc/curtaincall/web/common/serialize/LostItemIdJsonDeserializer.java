package org.cmc.curtaincall.web.common.serialize;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.cmc.curtaincall.domain.lostitem.LostItemId;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class LostItemIdJsonDeserializer extends JsonDeserializer<LostItemId> {

    @Override
    public LostItemId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        return new LostItemId(p.getLongValue());
    }
}
