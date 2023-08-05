package org.cmc.curtaincall.domain.show.repository;

import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowRepository extends JpaRepository<Show, String>, ShowRepositoryCustom {

    @EntityGraph(attributePaths = {"facility"})
    Slice<Show> findSliceWithFacilityByGenreAndUseYnIsTrue(Pageable pageable, ShowGenre genre);
}
