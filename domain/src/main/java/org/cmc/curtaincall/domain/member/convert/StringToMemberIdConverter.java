package org.cmc.curtaincall.domain.member.convert;

import org.cmc.curtaincall.domain.member.MemberId;
import org.springframework.core.convert.converter.Converter;

public class StringToMemberIdConverter implements Converter<String, MemberId> {

    @Override
    public MemberId convert(String source) {
        return new MemberId(Long.parseLong(source));
    }
}
