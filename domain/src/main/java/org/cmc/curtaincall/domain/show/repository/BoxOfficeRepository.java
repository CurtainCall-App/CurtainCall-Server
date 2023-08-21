package org.cmc.curtaincall.domain.show.repository;

import org.cmc.curtaincall.domain.show.BoxOffice;
import org.cmc.curtaincall.domain.show.BoxOfficeGenre;
import org.cmc.curtaincall.domain.show.BoxOfficeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BoxOfficeRepository extends JpaRepository<BoxOffice, String> {

    @EntityGraph(attributePaths = {"show"})
    List<BoxOffice> findAllWithShowByBaseDateAndTypeAndGenreOrderByRank(
            LocalDate baseDate, BoxOfficeType type, BoxOfficeGenre genre);
}
