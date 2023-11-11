package org.cmc.curtaincall.domain.show.event;

import org.cmc.curtaincall.domain.core.AbstractDomainEvent;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.domain.show.ShowState;

import java.time.LocalDate;

public class ShowCreatedEvent extends AbstractDomainEvent<ShowCreatedEvent.Source> {

    public ShowCreatedEvent(final ShowCreatedEvent.Source source) {
        super(source);
    }

    public record Source(
          ShowId id,
          ShowGenre genre,
          ShowState state,
          LocalDate startDate,
          LocalDate endDate
    ) {
    }

}
