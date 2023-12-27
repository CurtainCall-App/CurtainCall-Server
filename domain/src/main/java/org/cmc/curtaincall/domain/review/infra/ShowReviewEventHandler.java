package org.cmc.curtaincall.domain.review.infra;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.member.event.MemberWithdrewEvent;
import org.cmc.curtaincall.domain.review.ShowReviewStats;
import org.cmc.curtaincall.domain.review.repository.ShowReviewStatsRepository;
import org.cmc.curtaincall.domain.show.event.ShowCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.cmc.curtaincall.domain.review.QShowReview.showReview;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShowReviewEventHandler {

    private final ShowReviewStatsRepository showReviewStatsRepository;

    private final JPAQueryFactory query;

    @EventListener
    @Transactional
    public void handleShowCreatedEvent(final ShowCreatedEvent event) {
        log.debug("handleShowCreatedEvent={}", event);
        final ShowCreatedEvent.Source source = event.getSource();
        showReviewStatsRepository.findWithPessimisticLockById(source.id()).ifPresentOrElse(
                showReviewStats -> showReviewStats.update(
                        source.genre(), source.state(), source.startDate(), source.endDate()
                ), () -> showReviewStatsRepository.save(new ShowReviewStats(
                        source.id(), source.genre(), source.state(), source.startDate(), source.endDate()
                ))
        );
    }

    @EventListener
    @Transactional
    public void handleMemberWithdrewEvent(final MemberWithdrewEvent event) {
        log.debug("handleMemberWithdrewEvent. event={}", event);
        final MemberId memberId = event.getSource();

        query.update(showReview)
                .set(showReview.useYn, false)
                .where(
                        showReview.createdBy.memberId.eq(memberId)
                )
                .setLockMode(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
                .execute();
    }
}
