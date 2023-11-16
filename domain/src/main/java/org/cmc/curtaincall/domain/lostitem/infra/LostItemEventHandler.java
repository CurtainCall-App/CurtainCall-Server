package org.cmc.curtaincall.domain.lostitem.infra;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.member.event.MemberWithdrewEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.cmc.curtaincall.domain.lostitem.QLostItem.lostItem;

@Component
@RequiredArgsConstructor
@Slf4j
public class LostItemEventHandler {

    private final JPAQueryFactory query;

    @EventListener
    @Transactional
    public void handleMemberWithdrewEvent(final MemberWithdrewEvent event) {
        log.debug("handleMemberWithdrewEvent. event={}", event);
        final MemberId memberId = event.getSource();
        query.update(lostItem)
                .set(lostItem.useYn, false)
                .where(
                        lostItem.createdBy.memberId.eq(memberId)
                )
                .setLockMode(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
                .execute();
    }
}
