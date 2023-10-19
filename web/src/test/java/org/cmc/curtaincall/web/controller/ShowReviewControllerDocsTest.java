package org.cmc.curtaincall.web.controller;

import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.cmc.curtaincall.web.common.response.IdResult;
import org.cmc.curtaincall.web.service.review.ShowReviewService;
import org.cmc.curtaincall.web.service.review.request.ShowReviewCreate;
import org.cmc.curtaincall.web.service.review.request.ShowReviewEdit;
import org.cmc.curtaincall.web.service.review.response.ShowReviewResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

import static org.cmc.curtaincall.web.common.RestDocsAttribute.constraint;
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
                .andDo(document("show-review-create-show-review",
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
    void getList_Docs() throws Exception {
        // given
        List<ShowReviewResponse> reviewResponseList = List.of(
                ShowReviewResponse.builder()
                        .id(5L)
                        .showId("PF223355")
                        .grade(4)
                        .content("좋아요")
                        .creatorId(4L)
                        .creatorNickname("고라파덕")
                        .creatorImageUrl("http://image-url")
                        .build()
        );
        given(showReviewService.getList(any(), any()))
                .willReturn(new SliceImpl<>(reviewResponseList));

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
                .andDo(document("show-review-get-list",
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
                .andDo(document("show-review-delete-review",
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
                .andDo(document("show-review-edit-review",
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