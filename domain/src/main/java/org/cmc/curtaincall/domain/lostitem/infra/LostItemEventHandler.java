package org.cmc.curtaincall.domain.lostitem.infra;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LostItemEventHandler {

    private final JPAQueryFactory query;

}
