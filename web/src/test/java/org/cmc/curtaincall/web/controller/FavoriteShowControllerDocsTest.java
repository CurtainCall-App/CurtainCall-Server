package org.cmc.curtaincall.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cmc.curtaincall.web.common.RestDocsConfig;
import org.cmc.curtaincall.web.service.account.AccountService;
import org.cmc.curtaincall.web.service.show.FavoriteShowService;
import org.cmc.curtaincall.web.service.show.response.FavoriteShowResponse;
import org.cmc.curtaincall.web.service.show.response.ShowFavoriteResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(RestDocsConfig.class)
@AutoConfigureRestDocs
@WebMvcTest(FavoriteShowController.class)
class FavoriteShowControllerDocsTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AccountService accountService;

    @MockBean
    FavoriteShowService favoriteShowService;

    @Test
    @WithMockUser
    void favoriteShow_Docs() throws Exception {
        // given
        given(accountService.getMemberId(anyString())).willReturn(2L);

        // expected
        mockMvc.perform(put("/shows/{showId}/favorite", "PF220846")
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("favorite-show-favorite-show",
                        pathParameters(
                                parameterWithName("showId").description("공연 ID")
                        )
                ));
    }

    @Test
    @WithMockUser
    void cancelFavorite_Docs() throws Exception {
        // given
        given(accountService.getMemberId(anyString())).willReturn(2L);

        // expected
        mockMvc.perform(delete("/shows/{showId}/favorite", "PF220846")
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("favorite-show-cancel-favorite",
                        pathParameters(
                                parameterWithName("showId").description("공연 ID")
                        )
                ));
    }

    @Test
    @WithMockUser
    void getFavorite_Docs() throws Exception {
        // given
        given(accountService.getMemberId(anyString())).willReturn(2L);

        given(favoriteShowService.areFavorite(any(), any())).willReturn(
                List.of(
                        new ShowFavoriteResponse("PF220846", true),
                        new ShowFavoriteResponse("PF189549", false)
                )
        );

        // expected
        mockMvc.perform(get("/member/favorite")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("showIds", "PF220846", "PF189549")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("favorite-get-favorite",
                        queryParameters(
                                parameterWithName("showIds").description("공연 아이디 리스트")
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("showId").description("공연 ID"),
                                fieldWithPath("favorite").description("즐겨찾기 여부")
                        )
                ));
    }

    @Test
    @WithMockUser
    void getFavoriteShowList_Docs() throws Exception {
        // given
        given(accountService.getMemberId(anyString())).willReturn(2L);

        List<FavoriteShowResponse> favoriteShowResponseList = List.of(
                FavoriteShowResponse.builder()
                        .id("PF220846")
                        .name("잘자요, 엄마 [청주]")
                        .story("[시놉시스] 외딴곳, 시골에서 함께 살아가고 있는 모녀. 여느 때처럼 평범한 일상을 보내고 있던 토요일 저녁 모녀에게 위기가 닥친다. 느닷없이 던져진 제씨의 폭탄선언, 엄마를 위한다는 제씨의 행동은 엄마인 델마에겐 도저히 받아들일 수 없는데….")
                        .poster("http://www.kopis.or.kr/upload/pfmPoster/PF_PF220846_230704_164730.jpg")
                        .reviewCount(10)
                        .reviewGradeSum(45L)
                        .build()
        );
        given(favoriteShowService.getFavoriteShowList(any(), any())).willReturn(
                new SliceImpl<>(favoriteShowResponseList)
        );

        // expected
        mockMvc.perform(get("/members/{memberId}/favorite", 1L)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("favorite-get-favorite-list",
                        pathParameters(
                                parameterWithName("memberId").description("회원 ID")
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("id").description("공연 ID"),
                                fieldWithPath("name").description("공연 이름"),
                                fieldWithPath("story").description("공연 줄거리"),
                                fieldWithPath("poster").description("공연 포스터"),
                                fieldWithPath("reviewCount").description("공연 리뷰 개수"),
                                fieldWithPath("reviewGradeSum").description("공연 평점 합")
                        )
                ));
    }
}