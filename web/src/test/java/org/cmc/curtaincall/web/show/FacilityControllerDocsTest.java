package org.cmc.curtaincall.web.show;

import org.cmc.curtaincall.domain.show.FacilityId;
import org.cmc.curtaincall.domain.show.ShowDay;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowTime;
import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.cmc.curtaincall.web.show.response.FacilityDetailResponse;
import org.cmc.curtaincall.web.show.response.ShowResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDate;
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

@WebMvcTest(FacilityController.class)
class FacilityControllerDocsTest extends AbstractWebTest {

    @MockBean
    private FacilityService facilityService;

    @MockBean
    private ShowService showService;

    @Test
    void getFacilityDetail_Docs() throws Exception {
        // given
        FacilityDetailResponse response = FacilityDetailResponse.builder()
                .id(new FacilityId("FC001298"))
                .name("시온아트홀 (구. JK아트홀, 샘아트홀)")
                .hallNum(1)
                .characteristic("민간(대학로)")
                .openingYear(2017)
                .seatNum(134)
                .phone("070-8719-9106")
                .homepage("https://www.wwww.theblessed.net/")
                .address("서울특별시 종로구 대학로8가길 52 (동숭동)")
                .latitude(37.5829439)
                .longitude(127.00262709999992)
                .build();
        given(facilityService.getDetail(any())).willReturn(response);

        // expected
        mockMvc.perform(get("/facilities/{facilityId}", "FC001298")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("facility-get-facility-detail",
                        pathParameters(
                                parameterWithName("facilityId").description("공연 시설 아이디")
                        ),
                        responseFields(
                                fieldWithPath("id").description("공연장 ID"),
                                fieldWithPath("name").description("공연시설명"),
                                fieldWithPath("hallNum").description("공연장 수"),
                                fieldWithPath("characteristic").description("특징"),
                                fieldWithPath("openingYear").description("개관연도"),
                                fieldWithPath("seatNum").description("좌석수"),
                                fieldWithPath("phone").description("전화번호"),
                                fieldWithPath("homepage").description("홈페이지"),
                                fieldWithPath("address").description("주소"),
                                fieldWithPath("latitude").description("위도"),
                                fieldWithPath("longitude").description("경도")
                        )
                ));
    }

    @Test
    void getShowListOfFacility_Docs() throws Exception {
        // given
        var response = ShowResponse.builder()
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
                .build();

        given(showService.getListOfFacility(any(), any(), any()))
                .willReturn(new SliceImpl<>(List.of(response)));

        // expected
        mockMvc.perform(get("/facilities/{facilityId}/shows", "FC001298")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "20")
                        .param("genre", ShowGenre.PLAY.name())
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("facility-get-show-list-of-facility",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        pathParameters(
                                parameterWithName("facilityId").description("공연장 ID")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("페이지 사이즈").optional(),
                                parameterWithName("genre").description("공연 장르")
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
}