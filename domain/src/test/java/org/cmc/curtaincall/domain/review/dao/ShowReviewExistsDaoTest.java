package org.cmc.curtaincall.domain.review.dao;

import org.cmc.curtaincall.domain.common.AbstractDataJpaTest;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.review.ShowReview;
import org.cmc.curtaincall.domain.review.ShowReviewId;
import org.cmc.curtaincall.domain.show.ShowId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import(ShowReviewExistsDao.class)
class ShowReviewExistsDaoTest extends AbstractDataJpaTest {

    @Autowired
    private ShowReviewExistsDao showReviewExistsDao;

    @Test
    void existsByIdAndCreatedBy() {
        // given
        ShowReview showReview = new ShowReview(new ShowId("show-id"), 5, "content", new CreatorId(new MemberId(10L)));
        em.persist(showReview);

        em.flush();
        em.clear();

        // expected
        assertThat(showReviewExistsDao.existsByIdAndCreatedBy(
                new ShowReviewId(showReview.getId()), new CreatorId(new MemberId(10L)))
        ).isTrue();
    }

    @Test
    void existsByIdAndCreatedBy_ShowReviewIdDifferent_False() {
        // given
        ShowReview showReview = new ShowReview(new ShowId("show-id"), 5, "content", new CreatorId(new MemberId(10L)));
        em.persist(showReview);

        em.flush();
        em.clear();

        // expected
        assertThat(showReviewExistsDao.existsByIdAndCreatedBy(
                new ShowReviewId(showReview.getId() + 1), new CreatorId(new MemberId(10L)))
        ).isFalse();
    }

    @Test
    void existsByIdAndCreatedBy_CreatorIdDifferent_False() {
        // given
        ShowReview showReview = new ShowReview(new ShowId("show-id"), 5, "content", new CreatorId(new MemberId(10L)));
        em.persist(showReview);

        em.flush();
        em.clear();

        // expected
        assertThat(showReviewExistsDao.existsByIdAndCreatedBy(
                new ShowReviewId(showReview.getId()), new CreatorId(new MemberId(10L + 1)))
        ).isFalse();
    }
}