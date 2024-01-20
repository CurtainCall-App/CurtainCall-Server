package org.cmc.curtaincall.domain.recommend.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class ShowRecommendationDao {

    private final JPAQueryFactory query;

    public ShowRecommendationDao(final EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }
}
