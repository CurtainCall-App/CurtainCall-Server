package org.cmc.curtaincall.web.show;

import org.cmc.curtaincall.domain.show.BoxOfficeGenre;
import org.cmc.curtaincall.domain.show.BoxOfficeType;
import org.cmc.curtaincall.domain.show.ShowDay;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.domain.show.ShowTime;
import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.cmc.curtaincall.web.show.request.BoxOfficeRequest;
import org.cmc.curtaincall.web.show.response.BoxOfficeResponse;
import org.cmc.curtaincall.web.show.response.ShowResponse;
import org.cmc.curtaincall.web.show.response.ShowReviewStatsDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BoxOfficeController.class)
class BoxOfficeControllerDocsTest extends AbstractWebTest {

    @MockBean
    private BoxOfficeService boxOfficeService;

    @MockBean
    private ShowReviewStatsQueryService showReviewStatsQueryService;

    @MockBean
    private ShowService showService;

    @Test
    void getBoxOffice_Docs() throws Exception {
        // given
        var boxOfficeResponse = BoxOfficeResponse.builder()
                .id(new ShowId("PF223822"))
                .rank(1)
                .build();
        given(boxOfficeService.getList(new BoxOfficeRequest(
                BoxOfficeType.WEEK, LocalDate.of(2023, 8, 17), BoxOfficeGenre.PLAY))
        ).willReturn(List.of(boxOfficeResponse));

        final ShowResponse showResponse = ShowResponse.builder()
                .id(new ShowId("PF223822"))
                .name("그날밤, 너랑 나 [대학로]")
                .startDate(LocalDate.of(2023, 8, 14))
                .endDate(LocalDate.of(2023, 8, 31))
                .facilityName("대학로 아트포레스트")
                .poster("poster-url")
                .genre(ShowGenre.PLAY)
                .showTimes(List.of(
                        new ShowTime(ShowDay.FRIDAY, LocalTime.of(1, 30))
                ))
                .runtime("2시간 30분")
                .build();
        given(showService.getList(List.of(new ShowId("PF223822")))).willReturn(List.of(showResponse));


        final var showReviewStatsDto = ShowReviewStatsDto.builder()
                .showId(new ShowId("PF223822"))
                .reviewCount(10)
                .reviewGradeSum(48L)
                .reviewGradeAvg(((double) 10) / 48)
                .build();
        given(showReviewStatsQueryService.getList(List.of(new ShowId("PF223822")))).willReturn(
                List.of(showReviewStatsDto));

        // expected
        mockMvc.perform(get("/box-office")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("type", BoxOfficeType.WEEK.name())
                        .param("genre", BoxOfficeGenre.PLAY.name())
                        .param("baseDate", "2023-08-17")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("box-office-get-list",
                        queryParameters(
                                parameterWithName("type").description("일, 주, 월")
                                        .attributes(key("type").value(BoxOfficeType.class.getSimpleName())),
                                parameterWithName("baseDate").description("기준일"),
                                parameterWithName("genre").description("장르, null이면 전체 조회")
                                        .optional()
                                        .attributes(key("type").value(BoxOfficeGenre.class.getSimpleName()))
                                        .attributes(key("defaultValue").value(null))
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("id").description("공연 아이디"),
                                fieldWithPath("name").description("공연명"),
                                fieldWithPath("startDate").description("공연 시작일"),
                                fieldWithPath("endDate").description("공연 종료일"),
                                fieldWithPath("facilityName").description("공연 시설명(공연장명)"),
                                fieldWithPath("poster").description("공연 포스터 경로"),
                                fieldWithPath("genre").type(ShowGenre.class.getSimpleName()).description("공연 장르명"),
                                fieldWithPath("showTimes").description("공연 시간"),
                                fieldWithPath("showTimes[].dayOfWeek").description("공연 요일"),
                                fieldWithPath("showTimes[].time").description("공연 시간"),
                                fieldWithPath("runtime").description("공연 시간"),
                                fieldWithPath("reviewCount").description("리뷰 수"),
                                fieldWithPath("reviewGradeSum").description("리뷰 점수 합"),
                                fieldWithPath("reviewGradeAvg").description("리뷰 점수 평균"),
                                fieldWithPath("rank").description("순위")
                        )
                ));
    }
}