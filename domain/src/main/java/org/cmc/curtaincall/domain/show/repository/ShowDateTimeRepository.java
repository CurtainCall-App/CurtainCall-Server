package org.cmc.curtaincall.domain.show.repository;

import org.cmc.curtaincall.domain.show.ShowDateTime;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowDateTimeRepository extends JpaRepository<ShowDateTime, Long> {

    @EntityGraph(attributePaths = {"show", "show.facility"})
    List<ShowDateTime> findAllByShowAtAfterAndShowEndAtBefore(
            LocalDateTime showAt, LocalDateTime showEndAt);
}
