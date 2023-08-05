package org.cmc.curtaincall.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cmc.curtaincall.web.common.RestDocsConfig;
import org.cmc.curtaincall.web.service.common.response.IdResult;
import org.cmc.curtaincall.web.service.review.ShowReviewService;
import org.cmc.curtaincall.web.service.review.request.ShowReviewCreate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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
}