package org.cmc.curtaincall.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cmc.curtaincall.web.common.RestDocsConfig;
import org.cmc.curtaincall.web.service.account.AccountService;
import org.cmc.curtaincall.web.service.common.response.IdResult;
import org.cmc.curtaincall.web.service.review.ShowReviewService;
import org.cmc.curtaincall.web.service.review.request.ShowReviewCreate;
import org.cmc.curtaincall.web.service.review.request.ShowReviewEdit;
import org.cmc.curtaincall.web.service.review.response.ShowReviewResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(RestDocsConfig.class)
@AutoConfigureRestDocs
@WebMvcTest(ShowReviewController.class)
class ShowReviewControllerDocsTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ShowReviewService showReviewService;

    @MockBean
    AccountService accountService;

    @Test
    @WithMockUser
    void createShowReview_Docs() throws Exception {
        // given
        ShowReviewCreate showReviewCreate = ShowReviewCreate.builder()
                .grade(5)
                .content("조아유~~")
                .build();
        given(showReviewService.create(any(), any())).willReturn(new IdResult<>(10L));

        // expected
        mockMvc.perform(post("/shows/{showId}/reviews", "PF220846")
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(showReviewCreate))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("show-review-create-show-review",
                        pathParameters(
                                parameterWithName("showId").description("공연 아이디")
                        ),
                        requestFields(
                                fieldWithPath("grade").description("평점"),
                                fieldWithPath("content").description("내용")
                        ),
                        responseFields(
                                fieldWithPath("id").description("공연 리뷰 ID")
                        )
                ));
    }

    @Test
    @WithMockUser
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
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "20")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("show-review-get-list",
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
                                fieldWithPath("createdAt").description("생성일시")
                        )
                ));
    }

    @Test
    @WithMockUser
    void deleteReview_Docs() throws Exception {
        // given
        given(accountService.getMemberId(any())).willReturn(5L);

        given(showReviewService.isOwnedByMember(any(), any())).willReturn(true);

        // expected
        mockMvc.perform(delete("/reviews/{reviewId}", "10")
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("show-review-delete-review",
                        pathParameters(
                                parameterWithName("reviewId").description("리뷰 ID")
                        )
                ));
    }

    @Test
    @WithMockUser
    void editShowReview_Docs() throws Exception {
        // given
        ShowReviewEdit showReviewEdit = ShowReviewEdit.builder()
                .content("수정된 내용")
                .grade(4)
                .build();

        given(accountService.getMemberId(any())).willReturn(5L);

        given(showReviewService.isOwnedByMember(any(), any())).willReturn(true);

        // expected
        mockMvc.perform(patch("/reviews/{reviewId}", "10")
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(showReviewEdit))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("show-review-edit-review",
                        pathParameters(
                                parameterWithName("reviewId").description("리뷰 ID")
                        ),
                        requestFields(
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("grade").description("평점")
                        )
                ));
    }
}