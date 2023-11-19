package org.cmc.curtaincall.web.common.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.cmc.curtaincall.domain.member.MemberId;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class MemberIdJsonSerializer extends JsonSerializer<MemberId> {

    @Override
    public void serialize(MemberId value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(value.getId());
    }
}
