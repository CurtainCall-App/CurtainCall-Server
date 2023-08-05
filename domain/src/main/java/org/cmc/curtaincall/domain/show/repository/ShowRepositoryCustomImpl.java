package org.cmc.curtaincall.domain.show.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShowRepositoryCustomImpl implements ShowRepositoryCustom {

    private final JPAQueryFactory query;
}
