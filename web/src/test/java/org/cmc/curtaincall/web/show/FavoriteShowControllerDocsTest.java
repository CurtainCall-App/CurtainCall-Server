package org.cmc.curtaincall.web.show;

import org.cmc.curtaincall.domain.show.ShowDay;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowTime;
import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.cmc.curtaincall.web.show.FavoriteShowController;
import org.cmc.curtaincall.web.show.FavoriteShowService;
import org.cmc.curtaincall.web.show.response.FavoriteShowResponse;
import org.cmc.curtaincall.web.show.response.ShowFavoriteResponse;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FavoriteShowController.class)
class FavoriteShowControllerDocsTest extends AbstractWebTest {

    @MockBean
    private FavoriteShowService favoriteShowService;

    @Test
    void favoriteShow_Docs() throws Exception {
        // expected
        mockMvc.perform(put("/shows/{showId}/favorite", "PF220846")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("favorite-show-favorite-show",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        pathParameters(
                                parameterWithName("showId").description("공연 ID")
                        )
                ));
    }

    @Test
    void cancelFavorite_Docs() throws Exception {
        // expected
        mockMvc.perform(delete("/shows/{showId}/favorite", "PF220846")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("favorite-show-cancel-favorite",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        pathParameters(
                                parameterWithName("showId").description("공연 ID")
                        )
                ));
    }

    @Test
    void getFavorite_Docs() throws Exception {
        given(favoriteShowService.areFavorite(any(), any())).willReturn(
                List.of(
                        new ShowFavoriteResponse("PF220846", true),
                        new ShowFavoriteResponse("PF189549", false)
                )
        );

        // expected
        mockMvc.perform(get("/member/favorite")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("showIds", "PF220846", "PF189549")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("favorite-get-favorite",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        queryParameters(
                                parameterWithName("showIds").description("공연 아이디 리스트")
                                        .attributes(type(List.class))
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("showId").description("공연 ID"),
                                fieldWithPath("favorite").description("즐겨찾기 여부")
                        )
                ));
    }

    @Test
    void getFavoriteShowList_Docs() throws Exception {
        List<FavoriteShowResponse> favoriteShowResponseList = List.of(
                FavoriteShowResponse.builder()
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
        given(favoriteShowService.getFavoriteShowList(any(), any())).willReturn(
                new SliceImpl<>(favoriteShowResponseList)
        );

        // expected
        mockMvc.perform(get("/members/{memberId}/favorite", 1L)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("favorite-get-favorite-list",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        pathParameters(
                                parameterWithName("memberId").description("회원 ID")
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