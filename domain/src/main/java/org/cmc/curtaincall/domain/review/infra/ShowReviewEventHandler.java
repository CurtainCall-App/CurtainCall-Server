package org.cmc.curtaincall.domain.review.infra;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cmc.curtaincall.domain.review.ShowReviewStats;
import org.cmc.curtaincall.domain.review.repository.ShowReviewStatsRepository;
import org.cmc.curtaincall.domain.show.event.ShowCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

}
