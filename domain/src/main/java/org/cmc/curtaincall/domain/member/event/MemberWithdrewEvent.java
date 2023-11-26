package org.cmc.curtaincall.domain.member.event;

import org.cmc.curtaincall.domain.core.AbstractDomainEvent;
import org.cmc.curtaincall.domain.member.MemberId;

public class MemberWithdrewEvent extends AbstractDomainEvent<MemberId> {

    public MemberWithdrewEvent(final MemberId source) {
        super(source);
    }
}
