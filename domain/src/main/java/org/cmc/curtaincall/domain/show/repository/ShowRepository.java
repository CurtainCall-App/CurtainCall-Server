package org.cmc.curtaincall.domain.show.repository;

import org.cmc.curtaincall.domain.show.Facility;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ShowRepository extends JpaRepository<Show, String>, ShowRepositoryCustom {

    @EntityGraph(attributePaths = {"facility"})
    Slice<Show> findSliceWithFacilityByGenreAndUseYnIsTrue(Pageable pageable, ShowGenre genre);

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
}
