package org.cmc.curtaincall.web.review;

import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.cmc.curtaincall.web.common.response.IdResult;
import org.cmc.curtaincall.web.review.request.ShowReviewCreate;
import org.cmc.curtaincall.web.review.request.ShowReviewEdit;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.cmc.curtaincall.web.common.RestDocsAttribute.constraint;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShowReviewController.class)
class ShowReviewControllerDocsTest extends AbstractWebTest {

    @MockBean
    ShowReviewService showReviewService;

    @Test
    void createShowReview_Docs() throws Exception {
        // given
        ShowReviewCreate showReviewCreate = ShowReviewCreate.builder()
                .grade(5)
                .content("조아유~~")
                .build();
        given(showReviewService.create(any(), any())).willReturn(new IdResult<>(10L));

        // expected
        mockMvc.perform(post("/shows/{showId}/reviews", "PF220846")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(showReviewCreate))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("showreview-create-show-review",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        pathParameters(
                                parameterWithName("showId").description("공연 아이디")
                        ),
                        requestFields(
                                fieldWithPath("grade").description("평점")
                                        .attributes(constraint("PositiveOrZero, max=5")),
                                fieldWithPath("content").description("내용")
                                        .attributes(constraint("max=200"))
                        ),
                        responseFields(
                                fieldWithPath("id").description("공연 리뷰 ID")
                        )
                ));
    }

    @Test
    void deleteReview_Docs() throws Exception {
        // given
        given(showReviewService.isOwnedByMember(any(), any())).willReturn(true);

        // expected
        mockMvc.perform(delete("/reviews/{reviewId}", "10")
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("showreview-delete-review",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        pathParameters(
                                parameterWithName("reviewId").description("리뷰 ID")
                        )
                ));
    }

    @Test
    void editShowReview_Docs() throws Exception {
        // given
        ShowReviewEdit showReviewEdit = ShowReviewEdit.builder()
                .content("수정된 내용")
                .grade(4)
                .build();

        given(showReviewService.isOwnedByMember(any(), any())).willReturn(true);

        // expected
        mockMvc.perform(patch("/reviews/{reviewId}", "10")
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(showReviewEdit))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("showreview-edit-review",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        pathParameters(
                                parameterWithName("reviewId").description("리뷰 ID")
                        ),
                        requestFields(
                                fieldWithPath("grade").description("평점")
                                        .attributes(constraint("PositiveOrZero, max=5")),
                                fieldWithPath("content").description("내용")
                                        .attributes(constraint("max=200"))
                        )
                ));
    }
}