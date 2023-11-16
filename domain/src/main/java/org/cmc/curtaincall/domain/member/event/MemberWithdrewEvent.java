package org.cmc.curtaincall.domain.member.event;

import org.cmc.curtaincall.domain.core.AbstractDomainEvent;
import org.cmc.curtaincall.domain.member.MemberId;

// TODO event handler 작성
public class MemberWithdrewEvent extends AbstractDomainEvent<MemberId> {

    public MemberWithdrewEvent(final MemberId source) {
        super(source);
    }
}
