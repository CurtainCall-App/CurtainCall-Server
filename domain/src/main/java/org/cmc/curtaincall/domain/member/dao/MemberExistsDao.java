package org.cmc.curtaincall.domain.member.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.member.MemberId;
import org.springframework.stereotype.Repository;

import static org.cmc.curtaincall.domain.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberExistsDao {

    private final JPAQueryFactory query;

    public boolean exists(MemberId memberId) {
        return query
                .selectOne()
                .from(member)
                .where(member.id.eq(memberId.getId()))
                .fetchFirst() != null;
    }
}
