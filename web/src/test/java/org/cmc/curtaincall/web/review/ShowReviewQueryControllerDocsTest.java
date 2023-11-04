package org.cmc.curtaincall.web.review;

import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.review.dao.ShowReviewDao;
import org.cmc.curtaincall.domain.review.response.ShowReviewMyResponse;
import org.cmc.curtaincall.domain.review.response.ShowReviewResponse;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShowReviewQueryController.class)
class ShowReviewQueryControllerDocsTest extends AbstractWebTest {

    @MockBean
    private ShowReviewDao showReviewDao;

    @Test
    void getList_Docs() throws Exception {
        // given
        List<ShowReviewResponse> reviewResponseList = List.of(
                ShowReviewResponse.builder()
                        .id(5L)
                        .showId(new ShowId("PF223355"))
                        .grade(4)
                        .content("좋아요")
                        .creatorId(new CreatorId(4L))
                        .creatorNickname("고라파덕")
                        .creatorImageUrl("http://image-url")
                        .build()
        );
        given(showReviewDao.getList(any(), any()))
                .willReturn(reviewResponseList);

        // expected
        mockMvc.perform(get("/shows/{showId}/reviews", "PF220846")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "20")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andDo(document("showreview-get-list",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 사이즈").optional()
                        ),
                        pathParameters(
                                parameterWithName("showId").description("공연 ID")
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("id").description("공연 리뷰 ID"),
                                fieldWithPath("showId").description("공연 ID"),
                                fieldWithPath("grade").description("평점"),
                                fieldWithPath("content").description("리뷰 내용"),
                                fieldWithPath("creatorId").description("작성자 ID"),
                                fieldWithPath("creatorNickname").description("작성자 닉네임"),
                                fieldWithPath("creatorImageUrl").description("작성자 프로필 이미지").optional(),
                                fieldWithPath("createdAt").description("생성일시"),
                                fieldWithPath("likeCount").description("좋아요 개수")
                        )
                ));
    }

    @Test
    void getMyList_Docs() throws Exception {
        // given
        List<ShowReviewMyResponse> reviewResponseList = List.of(
                ShowReviewMyResponse.builder()
                        .id(5L)
                        .showId(new ShowId("PF223355"))
                        .showName("잘자요, 엄마 [청주]")
                        .grade(4)
                        .content("좋아요")
                        .createdAt(LocalDateTime.of(2023, 8, 31, 3, 28))
                        .likeCount(100)
                        .build()
        );
        given(showReviewDao.getMyList(notNull(), notNull())).willReturn(reviewResponseList);

        // expected
        mockMvc.perform(get("/member/reviews")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "20")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("showreview-get-my-list",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 사이즈").optional()
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("id").description("공연 리뷰 ID"),
                                fieldWithPath("showId").description("공연 ID"),
                                fieldWithPath("showName").description("공연 이름"),
                                fieldWithPath("grade").description("평점"),
                                fieldWithPath("content").description("리뷰 내용"),
                                fieldWithPath("createdAt").description("생성일시"),
                                fieldWithPath("likeCount").description("좋아요 개수")
                        )
                ));
    }
}