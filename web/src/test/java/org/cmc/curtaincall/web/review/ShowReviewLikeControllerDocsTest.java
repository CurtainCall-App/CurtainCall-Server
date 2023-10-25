package org.cmc.curtaincall.web.review;

import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.cmc.curtaincall.web.review.response.ShowReviewLikedResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

import static org.cmc.curtaincall.web.common.RestDocsAttribute.type;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    }

    @Test
    void cancelLike_Docs() throws Exception {
        // expected
        mockMvc.perform(delete("/reviews/{reviewId}/like", "10")
                        .with(csrf())
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
    }

    @Test
    void getFavorite_Docs() throws Exception {
        // given
        given(showReviewLikeService.areLiked(any(), any())).willReturn(
                List.of(
                        new ShowReviewLikedResponse(4L, true),
                        new ShowReviewLikedResponse(12L, false)
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