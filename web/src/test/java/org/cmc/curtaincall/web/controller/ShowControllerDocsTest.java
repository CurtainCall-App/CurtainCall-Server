package org.cmc.curtaincall.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cmc.curtaincall.web.common.RestDocsConfig;
import org.cmc.curtaincall.web.service.kopis.KopisService;
import org.cmc.curtaincall.web.service.kopis.ShowGenre;
import org.cmc.curtaincall.web.service.kopis.response.ShowDetailResponse;
import org.cmc.curtaincall.web.service.kopis.response.ShowResponse;
import org.cmc.curtaincall.web.service.kopis.response.ShowTime;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(RestDocsConfig.class)
@AutoConfigureRestDocs
@WebMvcTest(ShowController.class)
class ShowControllerDocsTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    KopisService kopisService;

    @Test
    @WithMockUser
    void getShows_Docs() throws Exception {
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

    @Test
    @WithMockUser
    void getShowDetail_Docs() throws Exception {
        // given
        ShowDetailResponse response = ShowDetailResponse.builder()
                .id("PF220846")
                .name("잘자요, 엄마 [청주]")
                .startDate(LocalDate.of(2023, 4, 28))
                .endDate(LocalDate.of(2023, 5, 12))
                .facilityId("FC000182")
                .facilityName("예술나눔 터 (예술나눔 터)")
                .crew(" ")
                .casts(List.of("이채윤", "정아름"))
                .runtime(" ")
                .age("만 7세 이상")
                .enterprise(" ")
                .ticketPrice("전석 20,000원")
                .poster("http://www.kopis.or.kr/upload/pfmPoster/PF_PF220846_230704_164730.jpg")
                .story("[시놉시스]\r\n외딴곳, 시골에서 함께 살아가고 있는 모녀. 여느 때처럼 평범한 일상을 보내고 있던 토요일 저녁 모녀에게 위기가 닥친다. 느닷없이 던져진 제씨의 폭탄선언, 엄마를 위한다는 제씨의 행동은 엄마인 델마에겐 도저히 받아들일 수 없는데….")
                .genre("연극")
                .state("공연완료")
                .openRun("N")
                .introductionImages(List.of("http://www.kopis.or.kr/upload/pfmIntroImage/PF_PF220846_230704_0447300.jpg"))
                .showTimes(List.of(
                        new ShowTime(DayOfWeek.MONDAY, LocalTime.of(19, 30)),
                        new ShowTime(DayOfWeek.TUESDAY, LocalTime.of(19, 30)),
                        new ShowTime(DayOfWeek.WEDNESDAY, LocalTime.of(19, 30)),
                        new ShowTime(DayOfWeek.THURSDAY, LocalTime.of(19, 30)),
                        new ShowTime(DayOfWeek.FRIDAY, LocalTime.of(19, 30)),
                        new ShowTime(DayOfWeek.SATURDAY, LocalTime.of(19, 30)),
                        new ShowTime(DayOfWeek.SUNDAY, LocalTime.of(16, 0))
                ))
                .build();
        given(kopisService.getShowDetail(any())).willReturn(response);

        // expected
        mockMvc.perform(get("/shows/{showId}", "PF220846")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("show-get-show-detail",
                        pathParameters(
                                parameterWithName("showId").description("공연 아이디")
                        ),
                        responseFields(
                                fieldWithPath("id").description("공연 ID"),
                                fieldWithPath("name").description("공연명"),
                                fieldWithPath("startDate").description("공연 시작일"),
                                fieldWithPath("endDate").description("공연 종료일"),
                                fieldWithPath("facilityId").description("공연 시설 ID"),
                                fieldWithPath("facilityName").description("공연 시설명"),
                                fieldWithPath("crew").description("공연 제작진"),
                                fieldWithPath("casts").description("공연 출연진"),
                                fieldWithPath("runtime").description("공연 런타임"),
                                fieldWithPath("age").description("공연 관람 연령"),
                                fieldWithPath("enterprise").description("제작사"),
                                fieldWithPath("ticketPrice").description("티켓 가격"),
                                fieldWithPath("poster").description("공연 포스터 경로"),
                                fieldWithPath("story").description("줄거리"),
                                fieldWithPath("genre").description("공연 장르명"),
                                fieldWithPath("state").description("공연 상태"),
                                fieldWithPath("openRun").description("오픈런"),
                                fieldWithPath("introductionImages").description("소개 이미지"),
                                fieldWithPath("showTimes[].dayOfWeek").description("공연 요일"),
                                fieldWithPath("showTimes[].time").description("공연 시간")
                        )
                ));
    }
}