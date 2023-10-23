package org.cmc.curtaincall.domain.show.repository;

import jakarta.persistence.LockModeType;
import org.cmc.curtaincall.domain.show.Facility;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowState;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.time.LocalDate;
import java.util.Optional;

public interface ShowRepository extends JpaRepository<Show, String>, ShowRepositoryCustom {

    @EntityGraph(attributePaths = {"facility"})
    Slice<Show> findSliceWithFacilityByGenreAndStateAndUseYnIsTrue(Pageable pageable, ShowGenre genre, ShowState state);

    @EntityGraph(attributePaths = {"facility"})
    Slice<Show> findSliceWithByNameStartsWithAndUseYnIsTrue(Pageable pageable, String name);

    @EntityGraph(attributePaths = {"facility"})
    Slice<Show> findSliceWithByStartDateGreaterThanEqualAndUseYnIsTrue(Pageable pageable, LocalDate startDate);

    @EntityGraph(attributePaths = {"facility"})
    Slice<Show> findSliceWithByEndDateGreaterThanEqualAndUseYnIsTrue(Pageable pageable, LocalDate endDate);

    @EntityGraph(attributePaths = {"facility"})
    Slice<Show> findSliceWithByGenreAndEndDateGreaterThanEqualAndUseYnIsTrue(
            Pageable pageable, ShowGenre genre, LocalDate endDate);

    Slice<Show> findSliceWithByFacilityAndUseYnIsTrue(Pageable pageable, Facility facility);

    Slice<Show> findSliceWithByFacilityAndGenreAndUseYnIsTrue(Pageable pageable, Facility facility, ShowGenre genre);

    @Lock(LockModeType.OPTIMISTIC)
    Optional<Show> findWithLockById(String id);
}
