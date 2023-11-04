package org.cmc.curtaincall.web.review;

import org.cmc.curtaincall.domain.review.ShowReviewId;
import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.cmc.curtaincall.web.review.response.ShowReviewLikedResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

import static org.cmc.curtaincall.web.common.RestDocsAttribute.type;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShowReviewLikeController.class)
class ShowReviewLikeControllerDocsTest extends AbstractWebTest {

    @MockBean
    private ShowReviewLikeService showReviewLikeService;

    @Test
    void likeReview_Docs() throws Exception {
        // expected
        mockMvc.perform(put("/reviews/{reviewId}/like", "10")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("show-review-like-review-like",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        pathParameters(
                                parameterWithName("reviewId").description("공연 리뷰 ID")
                        )
                ));
        then(showReviewLikeService).should(times(1)).like(
                LOGIN_MEMBER_ID, new ShowReviewId(10L));
    }

    @Test
    void cancelLike_Docs() throws Exception {
        // expected
        mockMvc.perform(delete("/reviews/{reviewId}/like", "10")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("show-review-like-cancel-like",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        pathParameters(
                                parameterWithName("reviewId").description("공연 리뷰 ID")
                        )
                ));
        then(showReviewLikeService).should(times(1)).cancelLike(
                LOGIN_MEMBER_ID, new ShowReviewId(10L));
    }

    @Test
    void getLiked_Docs() throws Exception {
        // given

        given(showReviewLikeService.areLiked(LOGIN_MEMBER_ID, List.of(new ShowReviewId(4L), new ShowReviewId(12L))))
                .willReturn(List.of(
                                new ShowReviewLikedResponse(new ShowReviewId(4L), true),
                                new ShowReviewLikedResponse(new ShowReviewId(12L), false)
                        )
                );

        // expected
        mockMvc.perform(get("/member/like")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("reviewIds", "4", "12")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].showReviewId").value(4L))
                .andExpect(jsonPath("$.content[0].liked").value(true))
                .andExpect(jsonPath("$.content[1].showReviewId").value(12L))
                .andExpect(jsonPath("$.content[1].liked").value(false))
                .andDo(document("show-review-like-get-liked",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        queryParameters(
                                parameterWithName("reviewIds").description("공연 리뷰 아이디 리스트")
                                        .attributes(type(List.class))
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("showReviewId").description("공연 리뷰 ID"),
                                fieldWithPath("liked").description("좋아요 여부")
                        )
                ));
    }
}