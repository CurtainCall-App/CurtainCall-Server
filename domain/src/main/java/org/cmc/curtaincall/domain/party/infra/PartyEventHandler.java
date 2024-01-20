package org.cmc.curtaincall.domain.party.infra;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.member.event.MemberWithdrewEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.cmc.curtaincall.domain.party.QParty.party;
import static org.cmc.curtaincall.domain.party.QPartyMember.partyMember;

@Component
@RequiredArgsConstructor
@Slf4j
public class PartyEventHandler {

    private final JPAQueryFactory query;

    @EventListener
    @Transactional
    public void handleMemberWithdrewEvent(final MemberWithdrewEvent event) {
        log.debug("handleMemberWithdrewEvent. event={}", event);
        final MemberId memberId = event.getSource();

        query.selectFrom(party)
                .join(partyMember)
                .where(partyMember.memberId.eq(memberId))
                .setLockMode(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
                .fetch()
                .forEach(item -> item.leave(memberId));

        query.update(party)
                .set(party.useYn, false)
                .where(
                        party.createdBy.memberId.eq(memberId)
                )
                .setLockMode(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
                .execute();
    }
}
