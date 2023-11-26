package org.cmc.curtaincall.domain.member.dao;

import org.assertj.core.api.Assertions;
import org.cmc.curtaincall.domain.common.AbstractDataJpaTest;
import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.member.MemberId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(MemberExistsDao.class)
class MemberExistsDaoTest extends AbstractDataJpaTest {

    @Autowired
    private MemberExistsDao memberExistsDao;

    @Test
    void exists() {
        // given
        Member member = Member.builder()
                .nickname("test-nickname")
                .build();
        em.persist(member);

        // expected
        Assertions.assertThat(memberExistsDao.exists(new MemberId(member.getId()))).isTrue();
    }

    @Test
    void exists_NotExists() {
        // given
        Member member = Member.builder()
                .nickname("test-nickname")
                .build();
        em.persist(member);

        // expected
        Assertions.assertThat(memberExistsDao.exists(new MemberId(member.getId() + 1L))).isFalse();
    }
}