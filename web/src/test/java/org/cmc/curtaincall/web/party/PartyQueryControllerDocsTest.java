package org.cmc.curtaincall.web.party;

import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.party.PartyId;
import org.cmc.curtaincall.domain.party.dao.PartyDao;
import org.cmc.curtaincall.domain.party.response.PartyDetailResponse;
import org.cmc.curtaincall.domain.party.response.PartyParticipatedResponse;
import org.cmc.curtaincall.domain.party.response.PartyParticipationResponse;
import org.cmc.curtaincall.domain.party.response.PartyRecruitmentResponse;
import org.cmc.curtaincall.domain.party.response.PartyResponse;
import org.cmc.curtaincall.domain.show.FacilityId;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

@WebMvcTest(PartyQueryController.class)
class PartyQueryControllerDocsTest extends AbstractWebTest {

    @MockBean
    private PartyDao partyDao;

    @Test
    void getPartyList_Docs() throws Exception {
        // given
        PartyResponse partyResponse = PartyResponse.builder()
                .id(10L)
                .title("공연 같이 보실분~")
                .curMemberNum(2)
                .maxMemberNum(5)
                .showAt(LocalDateTime.of(2023, 4, 28, 19, 30))
                .createdAt(LocalDateTime.of(2023, 4, 28, 11, 12, 28))
                .creatorId(new CreatorId(2L))
                .creatorNickname("고라파덕")
                .creatorImageUrl("creator-image-url")
                .showId(new ShowId("PF220846"))
                .showName("잘자요, 엄마 [청주]")
                .showPoster("post-image-url")
                .facilityId(new FacilityId("FC000182"))
                .facilityName("예술나눔 터 (예술나눔 터)")
                .build();
        given(partyDao.getList(any(), any())).willReturn(List.of(partyResponse));

        // expected
        mockMvc.perform(get("/parties")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "20")
                        .param("startDate", LocalDate.of(2023, 4, 28).toString())
                        .param("endDate", LocalDate.of(2023, 4, 30).toString())
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("party-get-party-list",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("페이지 사이즈").optional(),
                                parameterWithName("startDate").description("시작 일자").optional(),
                                parameterWithName("endDate").description("종료 일자").optional()
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("id").description("파티 ID"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("curMemberNum").description("현재 참여 인원 수"),
                                fieldWithPath("maxMemberNum").description("최대 참여 인원 수"),
                                fieldWithPath("showAt").description("공연 일시").optional(),
                                fieldWithPath("createdAt").description("작성 일시"),
                                fieldWithPath("creatorId").description("작성자 ID"),
                                fieldWithPath("creatorNickname").description("작성자 닉네임"),
                                fieldWithPath("creatorImageUrl").description("작성자 이미지 URL").optional(),
                                fieldWithPath("showId").description("공연 ID").optional(),
                                fieldWithPath("showName").description("공연 이름").optional(),
                                fieldWithPath("showPoster").description("공연 포스터").optional(),
                                fieldWithPath("facilityId").description("공연 시설 ID").optional(),
                                fieldWithPath("facilityName").description("공연 시설 이름").optional()
                        )
                ));
    }


    @Test
    void searchParty_Docs() throws Exception {
        // given
        PartyResponse partyResponse = PartyResponse.builder()
                .id(10L)
                .title("공연 같이 보실분~")
                .curMemberNum(2)
                .maxMemberNum(5)
                .showAt(LocalDateTime.of(2023, 4, 28, 19, 30))
                .createdAt(LocalDateTime.of(2023, 4, 28, 11, 12, 28))
                .creatorId(new CreatorId(2L))
                .creatorNickname("고라파덕")
                .creatorImageUrl("creator-image-url")
                .showId(new ShowId("PF220846"))
                .showName("잘자요, 엄마 [청주]")
                .showPoster("post-image-url")
                .facilityId(new FacilityId("FC000182"))
                .facilityName("예술나눔 터 (예술나눔 터)")
                .build();
        given(partyDao.search(any(), any())).willReturn(List.of(partyResponse));

        // expected
        mockMvc.perform(get("/search/party")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "20")
                        .param("keyword", "잘자요")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("party-search",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("페이지 사이즈").optional(),
                                parameterWithName("keyword").description("검색 키워드")
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("id").description("파티 ID"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("curMemberNum").description("현재 참여 인원 수"),
                                fieldWithPath("maxMemberNum").description("최대 참여 인원 수"),
                                fieldWithPath("showAt").description("공연 일시").optional(),
                                fieldWithPath("createdAt").description("작성 일시"),
                                fieldWithPath("creatorId").description("작성자 ID"),
                                fieldWithPath("creatorNickname").description("작성자 닉네임"),
                                fieldWithPath("creatorImageUrl").description("작성자 이미지 URL").optional(),
                                fieldWithPath("showId").description("공연 ID").optional(),
                                fieldWithPath("showName").description("공연 이름").optional(),
                                fieldWithPath("showPoster").description("공연 포스터").optional(),
                                fieldWithPath("facilityId").description("공연 시설 ID").optional(),
                                fieldWithPath("facilityName").description("공연 시설 이름").optional()
                        )
                ));
    }


    @Test
    void getPartyDetail_Docs() throws Exception {
        // given
        PartyDetailResponse partyDetailResponse = PartyDetailResponse.builder()
                .id(10L)
                .title("공연 같이 보실분~")
                .content("저랑 같이 봐요~")
                .curMemberNum(2)
                .maxMemberNum(5)
                .showAt(LocalDateTime.of(2023, 4, 28, 19, 30))
                .createdAt(LocalDateTime.of(2023, 4, 28, 11, 12, 28))
                .creatorId(new CreatorId(2L))
                .creatorNickname("고라파덕")
                .creatorImageUrl("creator-image-url")
                .showId(new ShowId("PF220846"))
                .showName("잘자요, 엄마 [청주]")
                .facilityId(new FacilityId("FC000182"))
                .facilityName("예술나눔 터 (예술나눔 터)")
                .build();
        given(partyDao.getDetail(any())).willReturn(partyDetailResponse);

        // expected
        mockMvc.perform(get("/parties/{partyId}", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("party-get-party-detail",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        pathParameters(
                                parameterWithName("partyId").description("공연 아이디")
                        ),
                        responseFields(
                                fieldWithPath("id").description("파티 ID"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("curMemberNum").description("현재 참여 인원 수"),
                                fieldWithPath("maxMemberNum").description("최대 참여 인원 수"),
                                fieldWithPath("showAt").description("공연 일시").optional(),
                                fieldWithPath("createdAt").description("작성 일시"),
                                fieldWithPath("creatorId").description("작성자 ID"),
                                fieldWithPath("creatorNickname").description("작성자 닉네임"),
                                fieldWithPath("creatorImageUrl").description("작성자 이미지 URL").optional(),
                                fieldWithPath("showId").description("공연 ID").optional(),
                                fieldWithPath("showName").description("공연 이름").optional(),
                                fieldWithPath("showPoster").description("공연 포스터").optional(),
                                fieldWithPath("facilityId").description("공연 시설 ID").optional(),
                                fieldWithPath("facilityName").description("공연 시설 이름").optional()
                        )
                ));
    }

    @Test
    void getRecruitmentList_Docs() throws Exception {
        // given
        var partyResponse = PartyRecruitmentResponse.builder()
                .id(10L)
                .title("공연 같이 보실분~")
                .content("저랑 같이 봐요~")
                .curMemberNum(2)
                .maxMemberNum(5)
                .showAt(LocalDateTime.of(2023, 4, 28, 19, 30))
                .createdAt(LocalDateTime.of(2023, 4, 28, 11, 12, 28))
                .showId(new ShowId("PF220846"))
                .showName("잘자요, 엄마 [청주]")
                .showPoster("post-image-url")
                .facilityId(new FacilityId("FC000182"))
                .facilityName("예술나눔 터 (예술나눔 터)")
                .build();
        given(partyDao.getRecruitmentList(any(), any()))
                .willReturn(List.of(partyResponse));

        // expected
        mockMvc.perform(get("/members/{memberId}/recruitments", 2L)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "20")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("party-get-recruitment-list",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        pathParameters(
                                parameterWithName("memberId").description("회원 ID")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("페이지 사이즈").optional()
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("id").description("파티 ID"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("curMemberNum").description("현재 참여 인원 수"),
                                fieldWithPath("maxMemberNum").description("최대 참여 인원 수"),
                                fieldWithPath("showAt").description("공연 일시"),
                                fieldWithPath("createdAt").description("작성 일시"),
                                fieldWithPath("showId").description("공연 ID"),
                                fieldWithPath("showName").description("공연 이름"),
                                fieldWithPath("showPoster").description("공연 포스터"),
                                fieldWithPath("facilityId").description("공연 시설 ID"),
                                fieldWithPath("facilityName").description("공연 시설 이름")
                        )
                ));
    }

    @Test
    void getParticipationList_Docs() throws Exception {
        // given
        var partyResponse = PartyParticipationResponse.builder()
                .id(10L)
                .title("공연 같이 보실분~")
                .content("저랑 같이 봐요~")
                .curMemberNum(2)
                .maxMemberNum(5)
                .showAt(LocalDateTime.of(2023, 4, 28, 19, 30))
                .createdAt(LocalDateTime.of(2023, 4, 28, 11, 12, 28))
                .creatorId(new CreatorId(2L))
                .creatorNickname("고라파덕")
                .creatorImageUrl("creator-image-url")
                .showId(new ShowId("PF220846"))
                .showName("잘자요, 엄마 [청주]")
                .showPoster("post-image-url")
                .facilityId(new FacilityId("FC000182"))
                .facilityName("예술나눔 터 (예술나눔 터)")
                .build();
        given(partyDao.getParticipationList(any(), any()))
                .willReturn(List.of(partyResponse));

        // expected
        mockMvc.perform(get("/members/{memberId}/participations", 2L)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "20")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("party-get-participation-list",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        pathParameters(
                                parameterWithName("memberId").description("회원 ID")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("페이지 사이즈").optional()
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("id").description("파티 ID"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용"),
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
    void getParticipated_Docs() throws Exception {
        // given
        given(partyDao.areParticipated(any(), any())).willReturn(
                List.of(
                        new PartyParticipatedResponse(new PartyId(4L), true),
                        new PartyParticipatedResponse(new PartyId(12L), false)
                )
        );

        // expected
        mockMvc.perform(get("/member/participated")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("partyIds", "4", "12")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("party-get-participated",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        queryParameters(
                                parameterWithName("partyIds").description("파티 ID 리스트")
                                        .attributes(type(List.class))
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("partyId").description("파티 ID"),
                                fieldWithPath("participated").description("참여 여부")
                        )
                ));
    }
}