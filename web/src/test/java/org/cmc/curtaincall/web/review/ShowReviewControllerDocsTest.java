package org.cmc.curtaincall.web.review;

import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.review.ShowReviewId;
import org.cmc.curtaincall.domain.review.validation.ShowReviewCreatorValidator;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.cmc.curtaincall.web.review.request.ShowReviewCreate;
import org.cmc.curtaincall.web.review.request.ShowReviewCreateDepr;
import org.cmc.curtaincall.web.review.request.ShowReviewEdit;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.cmc.curtaincall.web.common.RestDocsAttribute.constraint;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShowReviewController.class)
class ShowReviewControllerDocsTest extends AbstractWebTest {

    @MockBean
    private ShowReviewService showReviewService;

    @MockBean
    private ShowReviewCreatorValidator showReviewCreatorValidator;

    @Test
    void createShowReview_Docs() throws Exception {
        // given
        ShowReviewCreateDepr showReviewCreate = ShowReviewCreateDepr.builder()
                .grade(5)
                .content("조아유~~")
                .build();
        given(showReviewService.create(any(ShowId.class), any())).willReturn(new ShowReviewId(10L));

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
    void create_Docs() throws Exception {
        // given
        var showReviewCreate = ShowReviewCreate.builder()
                .showId(new ShowId("PF220846"))
                .grade(5)
                .content("조아유~~")
                .build();
        given(showReviewService.create(showReviewCreate, new CreatorId(LOGIN_MEMBER_ID))).willReturn(new ShowReviewId(10L));

        // expected
        mockMvc.perform(post("/review")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(showReviewCreate))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andDo(document("showreview-create",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        requestFields(
                                fieldWithPath("showId").description("공연 아이디"),
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
    void delete_Docs() throws Exception {
        // expected
        mockMvc.perform(delete("/reviews/{reviewId}", "10")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("showreview-delete",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        pathParameters(
                                parameterWithName("reviewId").description("리뷰 ID")
                        )
                ));

        InOrder inOrder = inOrder(showReviewCreatorValidator, showReviewService);
        ShowReviewId showReviewId = new ShowReviewId(10L);
        inOrder.verify(showReviewCreatorValidator).validate(showReviewId, new CreatorId(LOGIN_MEMBER_ID));
        inOrder.verify(showReviewService).delete(showReviewId);
    }

    @Test
    void edit_Docs() throws Exception {
        // given
        var showReviewEdit = ShowReviewEdit.builder()
                .content("수정된 내용")
                .grade(4)
                .build();

        // expected
        mockMvc.perform(patch("/reviews/{reviewId}", "10")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(showReviewEdit))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("showreview-edit",
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

        InOrder inOrder = inOrder(showReviewCreatorValidator, showReviewService);
        ShowReviewId showReviewId = new ShowReviewId(10L);
        inOrder.verify(showReviewCreatorValidator).validate(showReviewId, new CreatorId(LOGIN_MEMBER_ID));
        inOrder.verify(showReviewService).edit(showReviewId, showReviewEdit);
    }
}