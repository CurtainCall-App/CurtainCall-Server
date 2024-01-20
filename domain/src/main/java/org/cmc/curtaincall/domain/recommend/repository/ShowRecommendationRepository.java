package org.cmc.curtaincall.domain.recommend.repository;

import org.cmc.curtaincall.domain.recommend.ShowRecommendation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShowRecommendationRepository extends JpaRepository<ShowRecommendation, Long> {

    @EntityGraph(attributePaths = "show")
    List<ShowRecommendation> findAllByUseYnIsTrue();
}
