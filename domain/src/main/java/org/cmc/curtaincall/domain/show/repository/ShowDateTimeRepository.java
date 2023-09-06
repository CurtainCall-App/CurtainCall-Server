package org.cmc.curtaincall.domain.show.repository;

import org.cmc.curtaincall.domain.show.ShowDateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ShowDateTimeRepository extends JpaRepository<ShowDateTime, Long> {

    @EntityGraph(attributePaths = {"show", "show.facility"})
    Slice<ShowDateTime> findSliceByShowAtAfterAndShowEndAtBefore(
            Pageable pageable, LocalDateTime showAt, LocalDateTime showEndAt);
}