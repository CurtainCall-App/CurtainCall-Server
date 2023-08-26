package org.cmc.curtaincall.domain.show.repository;

import org.cmc.curtaincall.domain.show.BoxOffice;
import org.cmc.curtaincall.domain.show.BoxOfficeGenre;
import org.cmc.curtaincall.domain.show.BoxOfficeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BoxOfficeRepository extends JpaRepository<BoxOffice, String> {

    @EntityGraph(attributePaths = {"show"})
    List<BoxOffice> findAllWithShowByBaseDateAndTypeAndGenreOrderByRank(
            LocalDate baseDate, BoxOfficeType type, BoxOfficeGenre genre);

    @Query("""
        select b.baseDate
        from BoxOffice b
        where b.baseDate <= :baseDate and b.type = :type and b.genre = :genre
        order by b.baseDate desc
        limit 1
    """)
    LocalDate findTopBaseDate(
            @Param("baseDate") LocalDate baseDate,
            @Param("type") BoxOfficeType type,
            @Param("genre") BoxOfficeGenre genre
    );
}
