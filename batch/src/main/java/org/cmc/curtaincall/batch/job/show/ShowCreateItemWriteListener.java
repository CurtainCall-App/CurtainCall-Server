package org.cmc.curtaincall.batch.job.show;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.domain.show.event.ShowCreatedEvent;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.springframework.context.ApplicationEventPublisher;

@RequiredArgsConstructor
@Slf4j
public class ShowCreateItemWriteListener implements ItemWriteListener<Show> {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void afterWrite(final Chunk<? extends Show> items) {
        items.getItems().stream()
                .map(item -> new ShowCreatedEvent(new ShowCreatedEvent.Source(
                        new ShowId(item.getId()),
                        item.getGenre(),
                        item.getState()
                )))
                .forEach(event -> {
                    eventPublisher.publishEvent(event);
                    log.debug("publish event {}", event);
                });
    }
}
