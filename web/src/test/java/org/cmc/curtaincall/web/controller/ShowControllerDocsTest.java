package org.cmc.curtaincall.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowTime;
import org.cmc.curtaincall.web.common.RestDocsConfig;
import org.cmc.curtaincall.web.service.account.AccountService;
import org.cmc.curtaincall.web.service.kopis.KopisService;
import org.cmc.curtaincall.web.service.show.ShowService;
import org.cmc.curtaincall.web.service.show.response.ShowDetailResponse;
import org.cmc.curtaincall.web.service.show.response.ShowResponse;
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
    AccountService accountService;

    @MockBean
    KopisService kopisService;

    @MockBean
    ShowService showService;

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
                        .facilityName("예술나눔 터")
                        .poster("http://www.kopis.or.kr/upload/pfmPoster/PF_PF220846_230704_164730.jpg")
                        .genre(ShowGenre.PLAY)
                        .showTimes(List.of(
                                new ShowTime(DayOfWeek.WEDNESDAY, LocalTime.of(13, 30)),
                                new ShowTime(DayOfWeek.THURSDAY, LocalTime.of(13, 30)),
                                new ShowTime(DayOfWeek.SATURDAY, LocalTime.of(13, 30)),
                                new ShowTime(DayOfWeek.SATURDAY, LocalTime.of(19, 30)),
                                new ShowTime(DayOfWeek.SUNDAY, LocalTime.of(13, 30)),
                                new ShowTime(DayOfWeek.SUNDAY, LocalTime.of(19, 30))
                        ))
                        .build()
        );

        given(showService.getList(any(), any())).willReturn(new SliceImpl<>(showResponses));

        // expected
        mockMvc.perform(get("/shows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "20")
//                        .param("startDate", LocalDate.of(2023, 5, 5).toString())
//                        .param("endDate", LocalDate.of(2023, 5, 30).toString())
//                        .param("name", "잘자요")
                        .param("genre", ShowGenre.PLAY.name())
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("show-get-shows",
                        queryParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("페이지 사이즈").optional(),
                                parameterWithName("genre").description("공연 장르")
//                                parameterWithName("startDate").description("공연 시작일"),
//                                parameterWithName("endDate").description("공연 종료일"),
//                                parameterWithName("name").description("공연명").optional()
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("id").description("공연 아이디"),
                                fieldWithPath("name").description("공연명"),
                                fieldWithPath("startDate").description("공연 시작일"),
                                fieldWithPath("endDate").description("공연 종료일"),
                                fieldWithPath("facilityName").description("공연 시설명"),
                                fieldWithPath("poster").description("공연 포스터 경로"),
                                fieldWithPath("genre").description("공연 장르명"),
                                fieldWithPath("showTimes[].dayOfWeek").description("공연 요일"),
                                fieldWithPath("showTimes[].time").description("공연 시간")
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
                .crew("이봉규")
                .cast("이채윤, 정아름")
                .runtime("2시간 30분")
                .age("만 7세 이상")
                .enterprise(" ")
                .ticketPrice("전석 20,000원")
                .poster("http://www.kopis.or.kr/upload/pfmPoster/PF_PF220846_230704_164730.jpg")
                .story("[시놉시스]\r\n외딴곳, 시골에서 함께 살아가고 있는 모녀. 여느 때처럼 평범한 일상을 보내고 있던 토요일 저녁 모녀에게 위기가 닥친다. 느닷없이 던져진 제씨의 폭탄선언, 엄마를 위한다는 제씨의 행동은 엄마인 델마에겐 도저히 받아들일 수 없는데….")
                .genre(ShowGenre.PLAY)
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
                .reviewCount(0)
                .reviewGradeSum(0L)
                .build();
        given(showService.getDetail(any())).willReturn(response);

        // expected
        mockMvc.perform(get("/shows/{showId}", "PF220846")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
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
                                fieldWithPath("cast").description("공연 출연진"),
                                fieldWithPath("runtime").description("공연 런타임"),
                                fieldWithPath("age").description("공연 관람 연령"),
                                fieldWithPath("enterprise").description("제작사"),
                                fieldWithPath("ticketPrice").description("티켓 가격"),
                                fieldWithPath("poster").description("공연 포스터 경로"),
                                fieldWithPath("story").description("줄거리"),
                                fieldWithPath("genre").description("공연 장르명"),
                                fieldWithPath("introductionImages").description("소개 이미지"),
                                fieldWithPath("showTimes[].dayOfWeek").description("공연 요일"),
                                fieldWithPath("showTimes[].time").description("공연 시간"),
                                fieldWithPath("reviewCount").description("리뷰 수"),
                                fieldWithPath("reviewGradeSum").description("리뷰 점수 합")
                        )
                ));
    }

    @Test
    @WithMockUser
    void getBoxOffice_Docs() throws Exception {
//        // given
//        ShowBoxOfficeResponseList response = ShowBoxOfficeResponseList.builder()
//                .boxOffice(List.of(
//                        ShowBoxOfficeResponse.builder()
//                                .id("PF189859")
//                                .genre("뮤지컬")
//                                .rank(1)
//                                .name("웃는 남자")
//                                .period("2022.06.10~2022.08.22")
//                                .facilityName("세종문화회관 세종대극장")
//                                .seatCount(3022)
//                                .showCount(0)
//                                .area("서울")
//                                .poster("/upload/pfmPoster/PF_PF189859_220421_102833.gif")
//                                .build(),
//                        ShowBoxOfficeResponse.builder()
//                                .id("PF178981")
//                                .genre("뮤지컬")
//                                .rank(2)
//                                .name("지킬 앤 하이드")
//                                .period("2021.10.19~2022.05.08")
//                                .facilityName("샤롯데씨어터 샤롯데씨어터")
//                                .seatCount(1230)
//                                .showCount(0)
//                                .area("서울")
//                                .poster("/upload/pfmPoster/PF_PF178981_210827_112349.jpg")
//                                .build()
//                ))
//                .build();
//
//        given(kopisService.getBoxOffice(any())).willReturn(response);
//
//        // expected
//        mockMvc.perform(get("/boxOffice")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .param("baseDate", "2023-05-05")
//                        .param("type", ShowBoxOfficeRequest.Type.WEEK.name())
//                        .param("genre", ShowGenre.PLAY.name())
//                )
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andDo(document("show-get-boxOffice",
//                        queryParameters(
//                                parameterWithName("baseDate").description("기준 날짜"),
//                                parameterWithName("type").description("요청구분. MONTH, WEEK, DAY."),
//                                parameterWithName("genre").description("장르. PLAY, MUSICAL")
//                        ),
//                        responseFields(
//                                beneathPath("boxOffice[]").withSubsectionId("content"),
//                                fieldWithPath("id").description("공연 아이디"),
//                                fieldWithPath("genre").description("공연 장르"),
//                                fieldWithPath("rank").description("순위"),
//                                fieldWithPath("name").description("공연명"),
//                                fieldWithPath("period").description("공연 기간"),
//                                fieldWithPath("facilityName").description("공연 시설명"),
//                                fieldWithPath("seatCount").description("좌석수"),
//                                fieldWithPath("showCount").description("공연 횟수"),
//                                fieldWithPath("area").description("지역"),
//                                fieldWithPath("poster").description("포스터이미지")
//                        )
//                ));
    }
}