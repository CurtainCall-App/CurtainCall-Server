package org.cmc.curtaincall.domain.review.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ShowReviewDao {

    private final JPAQueryFactory query;
}
