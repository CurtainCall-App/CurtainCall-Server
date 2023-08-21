package org.cmc.curtaincall.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cmc.curtaincall.domain.show.BoxOfficeGenre;
import org.cmc.curtaincall.domain.show.BoxOfficeType;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.web.common.RestDocsConfig;
import org.cmc.curtaincall.web.service.account.AccountService;
import org.cmc.curtaincall.web.service.show.BoxOfficeService;
import org.cmc.curtaincall.web.service.show.response.BoxOfficeResponse;
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

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import(RestDocsConfig.class)
@AutoConfigureRestDocs
@WebMvcTest(BoxOfficeController.class)
class BoxOfficeControllerDocsTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AccountService accountService;

    @MockBean
    BoxOfficeService boxOfficeService;

    @Test
    @WithMockUser
    void getBoxOffice_Docs() throws Exception {
        // given
        BoxOfficeResponse boxOfficeResponse = BoxOfficeResponse.builder()
                .id("PF223822")
                .name("그날밤, 너랑 나 [대학로]")
                .startDate(LocalDate.of(2023, 8, 14))
                .endDate(LocalDate.of(2023, 8, 31))
                .poster("poster-url")
                .genre(ShowGenre.PLAY)
                .reviewGradeSum(10)
                .reviewCount(2)
                .rank(1)
                .build();
        given(boxOfficeService.getBoxOffice(any())).willReturn(List.of(boxOfficeResponse));

        // expected
        mockMvc.perform(get("/box-office")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("type", BoxOfficeType.WEEK.name())
                        .param("genre", BoxOfficeGenre.PLAY.name())
                        .param("baseDate", "2023-08-17")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("box-office-get-box-office",
                        queryParameters(
                                parameterWithName("type").description("일, 주, 월"),
                                parameterWithName("genre").description("조회하려는 요일"),
                                parameterWithName("baseDate").description("기준일")
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("id").description("공연 아이디"),
                                fieldWithPath("name").description("공연명"),
                                fieldWithPath("startDate").description("공연 시작일"),
                                fieldWithPath("endDate").description("공연 종료일"),
                                fieldWithPath("poster").description("공연 포스터 경로"),
                                fieldWithPath("genre").type(ShowGenre.class.getSimpleName()).description("공연 장르명"),
                                fieldWithPath("reviewCount").description("리뷰 수"),
                                fieldWithPath("reviewGradeSum").description("리뷰 점수 합"),
                                fieldWithPath("rank").description("순위")
                        )
                ));
    }
}