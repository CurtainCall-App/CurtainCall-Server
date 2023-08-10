package org.cmc.curtaincall.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cmc.curtaincall.web.common.RestDocsConfig;
import org.cmc.curtaincall.web.service.account.AccountService;
import org.cmc.curtaincall.web.service.show.FavoriteShowService;
import org.cmc.curtaincall.web.service.show.response.ShowFavoriteResponse;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
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
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
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
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
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
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
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
}