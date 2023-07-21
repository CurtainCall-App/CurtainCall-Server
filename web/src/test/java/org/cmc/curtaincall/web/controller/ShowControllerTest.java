package org.cmc.curtaincall.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cmc.curtaincall.web.common.RestDocsConfig;
import org.cmc.curtaincall.web.service.kopis.KopisService;
import org.cmc.curtaincall.web.service.kopis.ShowGenre;
import org.cmc.curtaincall.web.service.kopis.response.ShowResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(RestDocsConfig.class)
@AutoConfigureRestDocs
@WebMvcTest(ShowController.class)
class ShowControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    KopisService kopisService;

    @Test
    @WithMockUser
    void getShows() throws Exception {
        // given
        List<ShowResponse> showResponses = List.of(
                ShowResponse.builder()
                        .id("PF220846")
                        .name("잘자요, 엄마 [청주]")
                        .startDate(LocalDate.of(2023, 4, 28))
                        .endDate(LocalDate.of(2023, 5, 12))
                        .facility("예술나눔 터")
                        .poster("http://www.kopis.or.kr/upload/pfmPoster/PF_PF220846_230704_164730.jpg")
                        .genre("연극")
                        .state("공연완료")
                        .openRun("N")
                        .build()
        );
        given(kopisService.getShows(any(), any())).willReturn(new SliceImpl<>(showResponses));

        // expected
        mockMvc.perform(get("/shows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "1")
                        .param("size", "20")
                        .param("startDate", LocalDate.of(2023, 5, 5).toString())
                        .param("endDate", LocalDate.of(2023, 5, 30).toString())
                        .param("genre", ShowGenre.PLAY.name())
                        .param("name", "잘자요")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("show-get-shows",
                        queryParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("페이지 사이즈").optional(),
                                parameterWithName("startDate").description("공연 시작일"),
                                parameterWithName("endDate").description("공연 종료일"),
                                parameterWithName("genre").description("공연 장르"),
                                parameterWithName("name").description("공연명").optional()
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("id").description("공연 아이디"),
                                fieldWithPath("name").description("공연명"),
                                fieldWithPath("startDate").description("공연 시작일"),
                                fieldWithPath("endDate").description("공연 종료일"),
                                fieldWithPath("facility").description("공연 시설명"),
                                fieldWithPath("poster").description("공연 포스터 경로"),
                                fieldWithPath("genre").description("공연 장르명"),
                                fieldWithPath("state").description("공연 상태"),
                                fieldWithPath("openRun").description("오픈런")
                        )
                ));

    }
}