package org.cmc.curtaincall.web.show;

import org.cmc.curtaincall.domain.show.FacilityId;
import org.cmc.curtaincall.domain.show.ShowDay;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowState;
import org.cmc.curtaincall.domain.show.ShowTime;
import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.cmc.curtaincall.web.common.RestDocsAttribute;
import org.cmc.curtaincall.web.show.response.ShowDateTimeResponse;
import org.cmc.curtaincall.web.show.response.ShowDetailResponse;
import org.cmc.curtaincall.web.show.response.ShowResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.cmc.curtaincall.web.common.RestDocsAttribute.type;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShowController.class)
class ShowControllerDocsTest extends AbstractWebTest {

    @MockBean
    private ShowService showService;

    @Test
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
                                new ShowTime(ShowDay.WEDNESDAY, LocalTime.of(13, 30)),
                                new ShowTime(ShowDay.THURSDAY, LocalTime.of(13, 30)),
                                new ShowTime(ShowDay.SATURDAY, LocalTime.of(13, 30)),
                                new ShowTime(ShowDay.SATURDAY, LocalTime.of(19, 30)),
                                new ShowTime(ShowDay.SUNDAY, LocalTime.of(13, 30)),
                                new ShowTime(ShowDay.SUNDAY, LocalTime.of(19, 30))
                        ))
                        .reviewCount(10)
                        .reviewGradeSum(48L)
                        .runtime("1시간 40분")
                        .build()
        );

        given(showService.getList(any(), any())).willReturn(new SliceImpl<>(showResponses));

        // expected
        mockMvc.perform(get("/shows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "20")
                        .param("genre", ShowGenre.PLAY.name())
                        .param("sort", "reviewGradeAvg,desc")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("show-get-shows",
                        queryParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("페이지 사이즈").optional(),
                                parameterWithName("genre").description("공연 장르")
                                        .attributes(type(ShowGenre.class))
                                        .optional(),
                                parameterWithName("state").description("공연 상태").optional()
                                        .attributes(RestDocsAttribute.defaultValue(ShowState.PERFORMING.name())),
                                parameterWithName("sort").description(
                                        "reviewGradeAvg,desc: 공연 리뷰 평점 평균 내림차순 정렬, " +
                                        "name: 가나다순, " +
                                        "endDate: 마감일순"
                                ).optional()
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("id").description("공연 아이디"),
                                fieldWithPath("name").description("공연명"),
                                fieldWithPath("startDate").description("공연 시작일"),
                                fieldWithPath("endDate").description("공연 종료일"),
                                fieldWithPath("facilityName").description("공연 시설명"),
                                fieldWithPath("poster").description("공연 포스터 경로"),
                                fieldWithPath("genre").description("공연 장르명")
                                        .type(ShowGenre.class.getSimpleName()),
                                fieldWithPath("showTimes[].dayOfWeek").description("공연 요일")
                                        .type(ShowDay.class.getSimpleName()),
                                fieldWithPath("showTimes[].time").description("공연 시간"),
                                fieldWithPath("reviewCount").description("리뷰 수"),
                                fieldWithPath("reviewGradeSum").description("리뷰 점수 합"),
                                fieldWithPath("runtime").description("공연 런타임")
                        )
                ));
    }

    @Test
    void searchShows_Docs() throws Exception {
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
                                new ShowTime(ShowDay.WEDNESDAY, LocalTime.of(13, 30)),
                                new ShowTime(ShowDay.THURSDAY, LocalTime.of(13, 30)),
                                new ShowTime(ShowDay.SATURDAY, LocalTime.of(13, 30)),
                                new ShowTime(ShowDay.SATURDAY, LocalTime.of(19, 30)),
                                new ShowTime(ShowDay.SUNDAY, LocalTime.of(13, 30)),
                                new ShowTime(ShowDay.SUNDAY, LocalTime.of(19, 30))
                        ))
                        .reviewCount(10)
                        .reviewGradeSum(48L)
                        .runtime("1시간 40분")
                        .build()
        );

        given(showService.search(any(), any())).willReturn(new SliceImpl<>(showResponses));

        // expected
        mockMvc.perform(get("/search/shows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "20")
                        .param("keyword", "드림하이")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("show-search-shows",
                        queryParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("페이지 사이즈").optional(),
                                parameterWithName("keyword").description("검색어")
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("id").description("공연 아이디"),
                                fieldWithPath("name").description("공연명"),
                                fieldWithPath("startDate").description("공연 시작일"),
                                fieldWithPath("endDate").description("공연 종료일"),
                                fieldWithPath("facilityName").description("공연 시설명"),
                                fieldWithPath("poster").description("공연 포스터 경로"),
                                fieldWithPath("genre").description("공연 장르명")
                                        .type(ShowGenre.class.getSimpleName()),
                                fieldWithPath("showTimes[].dayOfWeek").description("공연 요일")
                                        .type(ShowDay.class.getSimpleName()),
                                fieldWithPath("showTimes[].time").description("공연 시간"),
                                fieldWithPath("reviewCount").description("리뷰 수"),
                                fieldWithPath("reviewGradeSum").description("리뷰 점수 합"),
                                fieldWithPath("runtime").description("공연 런타임")
                        )
                ));
    }

    @Test
    void getShowListToOpen_Docs() throws Exception {
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
                                new ShowTime(ShowDay.WEDNESDAY, LocalTime.of(13, 30)),
                                new ShowTime(ShowDay.THURSDAY, LocalTime.of(13, 30)),
                                new ShowTime(ShowDay.SATURDAY, LocalTime.of(13, 30)),
                                new ShowTime(ShowDay.SATURDAY, LocalTime.of(19, 30)),
                                new ShowTime(ShowDay.SUNDAY, LocalTime.of(13, 30)),
                                new ShowTime(ShowDay.SUNDAY, LocalTime.of(19, 30))
                        ))
                        .reviewCount(10)
                        .reviewGradeSum(48L)
                        .runtime("1시간 40분")
                        .build()
        );

        given(showService.getListToOpen(any(), any())).willReturn(new SliceImpl<>(showResponses));

        // expected
        mockMvc.perform(get("/shows-to-open")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "20")
                        .param("startDate", "2023-04-12")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-show-list-to-open",
                        queryParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("페이지 사이즈").optional(),
                                parameterWithName("startDate").description("기준일")
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("id").description("공연 아이디"),
                                fieldWithPath("name").description("공연명"),
                                fieldWithPath("startDate").description("공연 시작일"),
                                fieldWithPath("endDate").description("공연 종료일"),
                                fieldWithPath("facilityName").description("공연 시설명"),
                                fieldWithPath("poster").description("공연 포스터 경로"),
                                fieldWithPath("genre").description("공연 장르명")
                                        .type(ShowGenre.class.getSimpleName()),
                                fieldWithPath("showTimes[].dayOfWeek").description("공연 요일")
                                        .type(ShowDay.class.getSimpleName()),
                                fieldWithPath("showTimes[].time").description("공연 시간"),
                                fieldWithPath("reviewCount").description("리뷰 수"),
                                fieldWithPath("reviewGradeSum").description("리뷰 점수 합"),
                                fieldWithPath("runtime").description("공연 런타임")
                        )
                ));
    }

    @Test
    void getShowListToEnd_Docs() throws Exception {
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
                                new ShowTime(ShowDay.WEDNESDAY, LocalTime.of(13, 30)),
                                new ShowTime(ShowDay.THURSDAY, LocalTime.of(13, 30)),
                                new ShowTime(ShowDay.SATURDAY, LocalTime.of(13, 30)),
                                new ShowTime(ShowDay.SATURDAY, LocalTime.of(19, 30)),
                                new ShowTime(ShowDay.SUNDAY, LocalTime.of(13, 30)),
                                new ShowTime(ShowDay.SUNDAY, LocalTime.of(19, 30))
                        ))
                        .reviewCount(10)
                        .reviewGradeSum(48L)
                        .runtime("1시간 40분")
                        .build()
        );

        given(showService.getListToEnd(any(), any(), any())).willReturn(new SliceImpl<>(showResponses));

        // expected
        mockMvc.perform(get("/shows-to-end")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "20")
                        .param("endDate", "2023-04-12")
                        .param("genre", ShowGenre.PLAY.name())
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-show-list-to-end",
                        queryParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("페이지 사이즈").optional(),
                                parameterWithName("endDate").description("기준일"),
                                parameterWithName("genre").description("장르")
                                        .attributes(type(ShowGenre.class))
                                        .optional()
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("id").description("공연 아이디"),
                                fieldWithPath("name").description("공연명"),
                                fieldWithPath("startDate").description("공연 시작일"),
                                fieldWithPath("endDate").description("공연 종료일"),
                                fieldWithPath("facilityName").description("공연 시설명"),
                                fieldWithPath("poster").description("공연 포스터 경로"),
                                fieldWithPath("genre").description("공연 장르명")
                                        .type(ShowGenre.class.getSimpleName()),
                                fieldWithPath("showTimes[].dayOfWeek").description("공연 요일")
                                        .type(ShowDay.class.getSimpleName()),
                                fieldWithPath("showTimes[].time").description("공연 시간"),
                                fieldWithPath("reviewCount").description("리뷰 수"),
                                fieldWithPath("reviewGradeSum").description("리뷰 점수 합"),
                                fieldWithPath("runtime").description("공연 런타임")
                        )
                ));
    }

    @Test
    void getShowDetail_Docs() throws Exception {
        // given
        ShowDetailResponse response = ShowDetailResponse.builder()
                .id("PF220846")
                .name("잘자요, 엄마 [청주]")
                .startDate(LocalDate.of(2023, 4, 28))
                .endDate(LocalDate.of(2023, 5, 12))
                .facilityId(new FacilityId("FC000182"))
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
                        new ShowTime(ShowDay.MONDAY, LocalTime.of(19, 30)),
                        new ShowTime(ShowDay.TUESDAY, LocalTime.of(19, 30)),
                        new ShowTime(ShowDay.WEDNESDAY, LocalTime.of(19, 30)),
                        new ShowTime(ShowDay.THURSDAY, LocalTime.of(19, 30)),
                        new ShowTime(ShowDay.FRIDAY, LocalTime.of(19, 30)),
                        new ShowTime(ShowDay.SATURDAY, LocalTime.of(19, 30)),
                        new ShowTime(ShowDay.SUNDAY, LocalTime.of(16, 0))
                ))
                .reviewCount(10)
                .reviewGradeSum(48L)
                .build();
        given(showService.getDetail(any())).willReturn(response);

        // expected
        mockMvc.perform(get("/shows/{showId}", "PF220846")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
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
                                fieldWithPath("introductionImages").description("소개 이미지"),
                                fieldWithPath("genre").description("공연 장르명")
                                        .type(ShowGenre.class.getSimpleName()),
                                fieldWithPath("showTimes[].dayOfWeek").description("공연 요일")
                                        .type(ShowDay.class.getSimpleName()),
                                fieldWithPath("showTimes[].time").description("공연 시간"),
                                fieldWithPath("reviewCount").description("리뷰 수"),
                                fieldWithPath("reviewGradeSum").description("리뷰 점수 합")
                        )
                ));
    }

    @Test
    void getLiveTalkShowTimeList_Docs() throws Exception {
        // given
        var response = List.of(
                ShowDateTimeResponse.builder()
                        .id("PF220846")
                        .name("잘자요, 엄마 [청주]")
                        .facilityId(new FacilityId("FC000182"))
                        .facilityName("예술나눔 터")
                        .genre(ShowGenre.PLAY)
                        .poster("http://www.kopis.or.kr/upload/pfmPoster/PF_PF220846_230704_164730.jpg")
                        .showAt(LocalDateTime.of(2023, 4, 28, 19, 0))
                        .showEndAt(LocalDateTime.of(2023, 4, 28, 20, 30))
                        .build()
        );

        given(showService.getLiveTalkShowTimeList(any())).willReturn(response);

        // expected
        mockMvc.perform(get("/livetalk-show-times")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "20")
                        .param("baseDateTime", LocalDateTime.of(2023, 4, 13, 22, 0).toString())
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("show-get-show-time-list",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("페이지 사이즈").optional(),
                                parameterWithName("baseDateTime").description("기준 시간 (현재 시간)")
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("id").description("공연 아이디"),
                                fieldWithPath("name").description("공연명"),
                                fieldWithPath("facilityId").description("공연 시설 ID"),
                                fieldWithPath("facilityName").description("공연 시설명"),
                                fieldWithPath("poster").description("공연 포스터 경로"),
                                fieldWithPath("genre").description("공연 장르명")
                                        .type(ShowGenre.class.getSimpleName()),
                                fieldWithPath("showAt").description("공연 시작 일시"),
                                fieldWithPath("showEndAt").description("공연 종료 일시")
                        )
                ));
    }

}