package org.cmc.curtaincall.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cmc.curtaincall.domain.party.PartyCategory;
import org.cmc.curtaincall.web.common.RestDocsConfig;
import org.cmc.curtaincall.web.service.account.AccountService;
import org.cmc.curtaincall.web.service.common.response.IdResult;
import org.cmc.curtaincall.web.service.party.PartyService;
import org.cmc.curtaincall.web.service.party.request.PartyCreate;
import org.cmc.curtaincall.web.service.party.request.PartyEdit;
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

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(RestDocsConfig.class)
@AutoConfigureRestDocs
@WebMvcTest(PartyController.class)
class PartyControllerDocsTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AccountService accountService;

    @MockBean
    PartyService partyService;

    @Test
    @WithMockUser
    void createParty_Docs() throws Exception {
        // given
        PartyCreate partyCreate = PartyCreate.builder()
                .showId("PF220846")
                .showAt(LocalDateTime.of(2023, 4, 28, 19, 30))
                .title("공연 같이 보실분~")
                .content("저랑 같이 봐요~")
                .maxMemberNum(5)
                .category(PartyCategory.WATCHING)
                .build();
        given(partyService.create(any())).willReturn(new IdResult<>(10L));

        // expected
        mockMvc.perform(post("/parties")
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partyCreate))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("party-create-party",
                        requestFields(
                                fieldWithPath("showId").description("공연 ID"),
                                fieldWithPath("showAt").description("공연일시"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("maxMemberNum").description("최대 인원")
                                        .attributes(key("constraint").value("최대 10")),
                                fieldWithPath("category").description("분류")
                                        .attributes(key("constraint").value(PartyCategory.values()))
                        ),
                        responseFields(
                                fieldWithPath("id").description("파티 ID")
                        )
                ));
    }

    @Test
    @WithMockUser
    void deleteParty_Docs() throws Exception {
        // given
        given(accountService.getMemberId(any())).willReturn(5L);

        given(partyService.isOwnedByMember(any(), any())).willReturn(true);

        // expected
        mockMvc.perform(delete("/parties/{partyId}", 10)
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("party-delete-party",
                        pathParameters(
                                parameterWithName("partyId").description("파티 ID")
                        )
                ));
    }

    @Test
    @WithMockUser
    void editParty_Docs() throws Exception {
        // given
        given(accountService.getMemberId(any())).willReturn(5L);

        given(partyService.isOwnedByMember(any(), any())).willReturn(true);

        PartyEdit partyEdit = PartyEdit.builder()
                .title("수정 제목")
                .content("수정 내용")
                .build();

        // expected
        mockMvc.perform(patch("/parties/{partyId}", 10)
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partyEdit))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("party-edit-party",
                        pathParameters(
                                parameterWithName("partyId").description("파티 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용")
                        )
                ));
    }

    @Test
    @WithMockUser
    void participateParty_Docs() throws Exception {
        // given
        given(accountService.getMemberId(any())).willReturn(1L);

        // expected
        mockMvc.perform(put("/member/parties/{partyId}", 10)
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("party-participate-party",
                        pathParameters(
                                parameterWithName("partyId").description("파티 ID")
                        )
                ));
    }
}