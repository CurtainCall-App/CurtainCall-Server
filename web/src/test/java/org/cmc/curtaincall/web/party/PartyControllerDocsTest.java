package org.cmc.curtaincall.web.party;

import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.party.PartyCategory;
import org.cmc.curtaincall.domain.party.PartyId;
import org.cmc.curtaincall.domain.party.validation.PartyCreatorValidator;
import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.cmc.curtaincall.web.common.response.IdResult;
import org.cmc.curtaincall.web.party.request.PartyCreate;
import org.cmc.curtaincall.web.party.request.PartyEdit;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.cmc.curtaincall.web.common.RestDocsAttribute.constraint;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PartyController.class)
class PartyControllerDocsTest extends AbstractWebTest {

    @MockBean
    private PartyService partyService;

    @MockBean
    private PartyCreatorValidator partyCreatorValidator;

    @Test
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
        given(partyService.create(partyCreate)).willReturn(new IdResult<>(10L));

        // expected
        mockMvc.perform(post("/parties")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partyCreate))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andDo(print())
                .andDo(document("party-create-party",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        requestFields(
                                fieldWithPath("showId").description("공연 ID").optional(),
                                fieldWithPath("showAt").description("공연일시").optional(),
                                fieldWithPath("title").description("제목")
                                        .attributes(constraint("max = 100")),
                                fieldWithPath("content").description("내용")
                                        .attributes(constraint("max = 400")),
                                fieldWithPath("maxMemberNum").description("최대 인원")
                                        .attributes(constraint("min = 2, max = 100")),
                                fieldWithPath("category").description("분류")
                                        .type(PartyCategory.class.getSimpleName())
                        ),
                        responseFields(
                                fieldWithPath("id").description("파티 ID")
                        )
                ));
    }

    @Test
    void deleteParty_Docs() throws Exception {
        // given

        // expected
        mockMvc.perform(delete("/parties/{partyId}", 10)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("party-delete-party",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        pathParameters(
                                parameterWithName("partyId").description("파티 ID")
                        )
                ));
        PartyId partyId = new PartyId(10L);
        InOrder inOrder = inOrder(partyCreatorValidator, partyService);
        inOrder.verify(partyCreatorValidator).validate(partyId, new CreatorId(LOGIN_MEMBER_ID));
        inOrder.verify(partyService).delete(partyId);
    }

    @Test
    void editParty_Docs() throws Exception {
        // given
        PartyEdit partyEdit = PartyEdit.builder()
                .title("수정 제목")
                .content("수정 내용")
                .build();

        // expected
        mockMvc.perform(patch("/parties/{partyId}", 10)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partyEdit))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("party-edit-party",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        pathParameters(
                                parameterWithName("partyId").description("파티 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("제목")
                                        .attributes(constraint("max = 100")),
                                fieldWithPath("content").description("내용")
                                        .attributes(constraint("max = 400"))
                        )
                ));
        PartyId partyId = new PartyId(10L);
        InOrder inOrder = inOrder(partyCreatorValidator, partyService);
        inOrder.verify(partyCreatorValidator).validate(partyId, new CreatorId(LOGIN_MEMBER_ID));
        inOrder.verify(partyService).edit(partyId, partyEdit);
    }

    @Test
    void participateParty_Docs() throws Exception {
        // expected
        mockMvc.perform(put("/member/parties/{partyId}", 10)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("party-participate-party",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        pathParameters(
                                parameterWithName("partyId").description("파티 ID")
                        )
                ));
    }

}