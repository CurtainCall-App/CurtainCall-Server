package org.cmc.curtaincall.domain.review.dao;

import org.cmc.curtaincall.domain.common.AbstractDataJpaTest;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.review.ShowReview;
import org.cmc.curtaincall.domain.review.repository.ShowReviewLikeRepository;
import org.cmc.curtaincall.domain.review.response.ShowReviewMyResponse;
import org.cmc.curtaincall.domain.review.response.ShowReviewResponse;
import org.cmc.curtaincall.domain.show.Facility;
import org.cmc.curtaincall.domain.show.FacilityId;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.domain.show.ShowState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(ShowReviewDao.class)
class ShowReviewDaoTest extends AbstractDataJpaTest {

    @Autowired
    private ShowReviewDao showReviewDao;

    @Autowired
    private ShowReviewLikeRepository showReviewLikeRepository;

    @Test
    void getList() {
        // given
        Member member = Member.builder()
                .nickname("test-nickname")
                .build();
        em.persist(member);
        List<ShowReview> showReviews = List.of(
                ShowReview.builder()
                        .showId(new ShowId("test-show-id"))
                        .grade(1)
                        .content("test-content-1")
                        .createdBy(new CreatorId(new MemberId(member.getId())))
                        .build(),
                ShowReview.builder()
                        .showId(new ShowId("test-show-id"))
                        .grade(2)
                        .content("test-content-2")
                        .createdBy(new CreatorId(new MemberId(member.getId())))
                        .build(),
                ShowReview.builder()
                        .showId(new ShowId("different"))
                        .grade(3)
                        .content("test-content-3")
                        .createdBy(new CreatorId(new MemberId(member.getId())))
                        .build()
        );
        showReviews.forEach(em::persist);

        em.flush();
        em.clear();

        // when
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<ShowReviewResponse> result = showReviewDao.getList(pageRequest, new ShowId("test-show-id"));

        // then
        assertThat(result)
                .hasSize(2)
                .extracting("showId")
                .containsOnly(new ShowId("test-show-id"));
    }

    @Test
    void getList_LikeCountOrderDesc() {
        // given
        Member member = Member.builder()
                .nickname("test-nickname")
                .build();
        em.persist(member);
        List<ShowReview> showReviews = List.of(
                ShowReview.builder()
                        .showId(new ShowId("test-show-id"))
                        .grade(1)
                        .content("test-content-1")
                        .createdBy(new CreatorId(new MemberId(member.getId())))
                        .build(),
                ShowReview.builder()
                        .showId(new ShowId("test-show-id"))
                        .grade(2)
                        .content("test-content-2")
                        .createdBy(new CreatorId(new MemberId(member.getId())))
                        .build()
        );
        showReviews.forEach(em::persist);
        showReviews.get(1).like(new MemberId(10L), showReviewLikeRepository);

        em.flush();
        em.clear();

        // when
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("likeCount")));
        List<ShowReviewResponse> result = showReviewDao.getList(pageRequest, new ShowId("test-show-id"));

        // then
        assertThat(result)
                .hasSize(2)
                .extracting("likeCount")
                .containsExactly(1, 0);
    }

    @Test
    void getMyList() {
        // given
        em.persist(Show.builder()
                .id(new ShowId("show-id"))
                .facility(em.getReference(Facility.class, new FacilityId("facility-id")))
                .name("show-name")
                .startDate(LocalDate.of(2023, 10, 10))
                .endDate(LocalDate.of(2023, 10, 10))
                .crew("crew")
                .cast("cast")
                .runtime("runtime")
                .age("age")
                .enterprise("enterprise")
                .ticketPrice("ticketPrice")
                .poster("poster")
                .story("story")
                .state(ShowState.PERFORMING)
                .openRun("openrun")
                .genre(ShowGenre.MUSICAL)
                .build()
        );
        ShowReview showReview1 = ShowReview.builder()
                .showId(new ShowId("show-id"))
                .grade(1)
                .content("test-content-1")
                .createdBy(new CreatorId(new MemberId(10L)))
                .build();
        em.persist(showReview1);

        ShowReview showReview2 = ShowReview.builder()
                .showId(new ShowId("test-show-id"))
                .grade(2)
                .content("test-content-2")
                .createdBy(new CreatorId(new MemberId(20L)))
                .build();
        em.persist(showReview2);

        // when
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<ShowReviewMyResponse> result = showReviewDao.getMyList(
                pageRequest, new CreatorId(new MemberId(10L)));

        // then
        assertThat(result).hasSize(1);
        assertThat(result.stream().map(ShowReviewMyResponse::getId))
                .containsExactly(showReview1.getId());
        assertThat(result.stream().map(ShowReviewMyResponse::getShowId))
                .containsExactly(new ShowId("show-id"));
        assertThat(result.stream().map(ShowReviewMyResponse::getShowName))
                .containsExactly("show-name");
        assertThat(result.stream().map(ShowReviewMyResponse::getGrade))
                .containsExactly(1);
        assertThat(result.stream().map(ShowReviewMyResponse::getContent))
                .containsExactly("test-content-1");
    }
}