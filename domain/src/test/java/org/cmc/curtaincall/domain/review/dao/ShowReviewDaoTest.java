package org.cmc.curtaincall.domain.review.dao;

import org.cmc.curtaincall.domain.common.AbstractDataJpaTest;
import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.review.ShowReview;
import org.cmc.curtaincall.domain.review.response.ShowReviewResponse;
import org.cmc.curtaincall.domain.show.ShowId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@Import(ShowReviewDao.class)
class ShowReviewDaoTest extends AbstractDataJpaTest {

    @Autowired
    private ShowReviewDao showReviewDao;

    @Test
    void getList() {
        // given
        Member member = Member.builder()
                .nickname("test-nickname")
                .build();
        em.persist(member);
        given(auditorProvider.getCurrentAuditor()).willReturn(Optional.of(member));

        List<ShowReview> showReviews = List.of(
                ShowReview.builder()
                        .showId(new ShowId("test-show-id"))
                        .grade(1)
                        .content("test-content-1")
                        .build(),
                ShowReview.builder()
                        .showId(new ShowId("test-show-id"))
                        .grade(2)
                        .content("test-content-2")
                        .build(),
                ShowReview.builder()
                        .showId(new ShowId("different"))
                        .grade(3)
                        .content("test-content-3")
                        .build()
        );
        showReviews.forEach(em::persist);


        // when
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<ShowReviewResponse> result = showReviewDao.getList(pageRequest, new ShowId("test-show-id"));

        // then
        assertThat(result)
                .hasSize(2)
                .extracting("showId")
                .containsOnly("test-show-id");
    }

    @Test
    void getList_LikeCountOrderDesc() {
        // given
        Member member = Member.builder()
                .nickname("test-nickname")
                .build();
        em.persist(member);
        given(auditorProvider.getCurrentAuditor()).willReturn(Optional.of(member));

        List<ShowReview> showReviews = List.of(
                ShowReview.builder()
                        .showId(new ShowId("test-show-id"))
                        .grade(1)
                        .content("test-content-1")
                        .build(),
                ShowReview.builder()
                        .showId(new ShowId("test-show-id"))
                        .grade(2)
                        .content("test-content-2")
                        .build()
        );
        showReviews.get(1).plusLikeCount();
        showReviews.forEach(em::persist);

        // when
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("likeCount")));
        List<ShowReviewResponse> result = showReviewDao.getList(pageRequest, new ShowId("test-show-id"));

        // then
        assertThat(result)
                .hasSize(2)
                .extracting("likeCount")
                .containsExactly(1, 0);
    }
}