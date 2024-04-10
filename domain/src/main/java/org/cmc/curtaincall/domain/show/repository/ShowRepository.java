package org.cmc.curtaincall.domain.show.repository;

import jakarta.persistence.LockModeType;
import org.cmc.curtaincall.domain.show.Facility;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.domain.show.ShowState;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ShowRepository extends JpaRepository<Show, ShowId> {

    @EntityGraph(attributePaths = {"facility"})
    List<Show> findAllWithFacilityByGenreAndStateAndUseYnIsTrue(Pageable pageable, ShowGenre genre, ShowState state);

    @EntityGraph(attributePaths = {"facility"})
    List<Show> findAllWithByNameStartsWithAndUseYnIsTrue(Pageable pageable, String name);

    @EntityGraph(attributePaths = {"facility"})
    List<Show> findAllWithByStartDateGreaterThanEqualAndUseYnIsTrue(Pageable pageable, LocalDate startDate);

    @EntityGraph(attributePaths = {"facility"})
    List<Show> findAllWithByEndDateGreaterThanEqualAndUseYnIsTrue(Pageable pageable, LocalDate endDate);

    @EntityGraph(attributePaths = {"facility"})
    List<Show> findAllWithByGenreAndEndDateGreaterThanEqualAndUseYnIsTrue(
            Pageable pageable, ShowGenre genre, LocalDate endDate);

    List<Show> findAllWithByFacilityAndUseYnIsTrue(Pageable pageable, Facility facility);

    List<Show> findAllWithByFacilityAndGenreAndUseYnIsTrue(Pageable pageable, Facility facility, ShowGenre genre);

    @Lock(LockModeType.OPTIMISTIC)
    Optional<Show> findWithLockById(ShowId id);

    @EntityGraph(attributePaths = {"facility"})
    List<Show> findAllWithFacilityByIdIn(List<ShowId> id);
}
