package org.cmc.curtaincall.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cmc.curtaincall.domain.party.PartyCategory;
import org.cmc.curtaincall.web.common.RestDocsConfig;
import org.cmc.curtaincall.web.service.account.AccountService;
import org.cmc.curtaincall.web.service.common.response.IdResult;
import org.cmc.curtaincall.web.service.party.PartyService;
import org.cmc.curtaincall.web.service.party.request.PartyCreate;
import org.cmc.curtaincall.web.service.party.request.PartyEdit;
import org.cmc.curtaincall.web.service.party.response.PartyDetailResponse;
import org.cmc.curtaincall.web.service.party.response.PartyParticipatedResponse;
import org.cmc.curtaincall.web.service.party.response.PartyResponse;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
    void getPartyList_Docs() throws Exception {
        // given
        PartyResponse partyResponse = PartyResponse.builder()
                .id(10L)
                .title("공연 같이 보실분~")
                .curMemberNum(2)
                .maxMemberNum(5)
                .showAt(LocalDateTime.of(2023, 4, 28, 19, 30))
                .createdAt(LocalDateTime.of(2023, 4, 28, 11, 12, 28))
                .category(PartyCategory.WATCHING)
                .creatorId(2L)
                .creatorNickname("고라파덕")
                .creatorImageUrl("creator-image-url")
                .showId("PF220846")
                .showName("잘자요, 엄마 [청주]")
                .showPoster("post-image-url")
                .facilityId("FC000182")
                .facilityName("예술나눔 터 (예술나눔 터)")
                .build();
        given(partyService.getList(any(), any())).willReturn(new SliceImpl<>(List.of(partyResponse)));

        // expected
        mockMvc.perform(get("/parties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "20")
                        .param("category", PartyCategory.WATCHING.name())
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("party-get-party-list",
                        queryParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("페이지 사이즈").optional(),
                                parameterWithName("category").description("카테고리")
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("id").description("파티 ID"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("curMemberNum").description("현재 참여 인원 수"),
                                fieldWithPath("maxMemberNum").description("최대 참여 인원 수"),
                                fieldWithPath("showAt").description("공연 일시"),
                                fieldWithPath("createdAt").description("작성 일시"),
                                fieldWithPath("category").type(PartyCategory.class.getSimpleName())
                                        .description("카테고리"),
                                fieldWithPath("creatorId").description("작성자 ID"),
                                fieldWithPath("creatorNickname").description("작성자 닉네임"),
                                fieldWithPath("creatorImageUrl").description("작성자 이미지 URL").optional(),
                                fieldWithPath("showId").description("공연 ID"),
                                fieldWithPath("showName").description("공연 이름"),
                                fieldWithPath("showPoster").description("공연 포스터"),
                                fieldWithPath("facilityId").description("공연 시설 ID"),
                                fieldWithPath("facilityName").description("공연 시설 이름")
                        )
                ));
    }


    @Test
    @WithMockUser
    void searchParty_Docs() throws Exception {
        // given
        PartyResponse partyResponse = PartyResponse.builder()
                .id(10L)
                .title("공연 같이 보실분~")
                .curMemberNum(2)
                .maxMemberNum(5)
                .showAt(LocalDateTime.of(2023, 4, 28, 19, 30))
                .createdAt(LocalDateTime.of(2023, 4, 28, 11, 12, 28))
                .category(PartyCategory.WATCHING)
                .creatorId(2L)
                .creatorNickname("고라파덕")
                .creatorImageUrl("creator-image-url")
                .showId("PF220846")
                .showName("잘자요, 엄마 [청주]")
                .showPoster("post-image-url")
                .facilityId("FC000182")
                .facilityName("예술나눔 터 (예술나눔 터)")
                .build();
        given(partyService.search(any(), any())).willReturn(new SliceImpl<>(List.of(partyResponse)));

        // expected
        mockMvc.perform(get("/search/party")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "20")
                        .param("category", PartyCategory.WATCHING.name())
                        .param("keyword", "잘자요")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("party-search",
                        queryParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("페이지 사이즈").optional(),
                                parameterWithName("category").description("카테고리"),
                                parameterWithName("keyword").description("검색 키워드")
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("id").description("파티 ID"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("curMemberNum").description("현재 참여 인원 수"),
                                fieldWithPath("maxMemberNum").description("최대 참여 인원 수"),
                                fieldWithPath("showAt").description("공연 일시"),
                                fieldWithPath("createdAt").description("작성 일시"),
                                fieldWithPath("category").type(PartyCategory.class.getSimpleName())
                                        .description("카테고리"),
                                fieldWithPath("creatorId").description("작성자 ID"),
                                fieldWithPath("creatorNickname").description("작성자 닉네임"),
                                fieldWithPath("creatorImageUrl").description("작성자 이미지 URL").optional(),
                                fieldWithPath("showId").description("공연 ID"),
                                fieldWithPath("showName").description("공연 이름"),
                                fieldWithPath("showPoster").description("공연 포스터"),
                                fieldWithPath("facilityId").description("공연 시설 ID"),
                                fieldWithPath("facilityName").description("공연 시설 이름")
                        )
                ));
    }


    @Test
    @WithMockUser
    void getPartyDetail_Docs() throws Exception {
        // given
        PartyDetailResponse partyDetailResponse = PartyDetailResponse.builder()
                .id(10L)
                .title("공연 같이 보실분~")
                .content("저랑 같이 봐요~")
                .category(PartyCategory.WATCHING)
                .curMemberNum(2)
                .maxMemberNum(5)
                .showAt(LocalDateTime.of(2023, 4, 28, 19, 30))
                .createdAt(LocalDateTime.of(2023, 4, 28, 11, 12, 28))
                .creatorId(2L)
                .creatorNickname("고라파덕")
                .creatorImageUrl("creator-image-url")
                .showId("PF220846")
                .showName("잘자요, 엄마 [청주]")
                .facilityId("FC000182")
                .facilityName("예술나눔 터 (예술나눔 터)")
                .build();
        given(partyService.getDetail(any())).willReturn(partyDetailResponse);

        // expected
        mockMvc.perform(get("/parties/{partyId}", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("party-get-party-detail",
                        pathParameters(
                                parameterWithName("partyId").description("공연 아이디")
                        ),
                        responseFields(
                                fieldWithPath("id").description("파티 ID"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("category").type(PartyCategory.class.getSimpleName())
                                        .description("카테고리"),
                                fieldWithPath("curMemberNum").description("현재 참여 인원 수"),
                                fieldWithPath("maxMemberNum").description("최대 참여 인원 수"),
                                fieldWithPath("showAt").description("공연 일시"),
                                fieldWithPath("createdAt").description("작성 일시"),
                                fieldWithPath("creatorId").description("작성자 ID"),
                                fieldWithPath("creatorNickname").description("작성자 닉네임"),
                                fieldWithPath("creatorImageUrl").description("작성자 이미지 URL").optional(),
                                fieldWithPath("showId").description("공연 ID"),
                                fieldWithPath("showName").description("공연 이름"),
                                fieldWithPath("showPoster").description("공연 포스터"),
                                fieldWithPath("facilityId").description("공연 시설 ID"),
                                fieldWithPath("facilityName").description("공연 시설 이름")
                        )
                ));
    }

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
                                fieldWithPath("category").type(PartyCategory.class.getSimpleName())
                                        .description("분류")
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

    @Test
    @WithMockUser
    void getParticipated_Docs() throws Exception {
        // given
        given(accountService.getMemberId(any())).willReturn(2L);

        given(partyService.areParticipated(any(), any())).willReturn(
                List.of(
                        new PartyParticipatedResponse(4L, true),
                        new PartyParticipatedResponse(12L, false)
                )
        );

        // expected
        mockMvc.perform(get("/member/participated")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("partyIds", "4", "12")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("party-get-participated",
                        queryParameters(
                                parameterWithName("partyIds").description("파티 ID 리스트")
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("partyId").description("파티 ID"),
                                fieldWithPath("participated").description("참여 여부")
                        )
                ));
    }
}