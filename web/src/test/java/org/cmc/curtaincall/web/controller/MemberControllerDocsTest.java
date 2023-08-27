package org.cmc.curtaincall.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cmc.curtaincall.domain.member.MemberDeleteReason;
import org.cmc.curtaincall.domain.party.PartyCategory;
import org.cmc.curtaincall.web.common.RestDocsAttribute;
import org.cmc.curtaincall.web.common.RestDocsConfig;
import org.cmc.curtaincall.web.service.account.AccountService;
import org.cmc.curtaincall.web.service.common.response.BooleanResult;
import org.cmc.curtaincall.web.service.common.response.IdResult;
import org.cmc.curtaincall.web.service.image.ImageService;
import org.cmc.curtaincall.web.service.member.MemberService;
import org.cmc.curtaincall.web.service.member.request.MemberCreate;
import org.cmc.curtaincall.web.service.member.request.MemberDelete;
import org.cmc.curtaincall.web.service.member.request.MemberEdit;
import org.cmc.curtaincall.web.service.member.response.MemberDetailResponse;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import(RestDocsConfig.class)
@AutoConfigureRestDocs
@WebMvcTest(MemberController.class)
class MemberControllerDocsTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MemberService memberService;

    @MockBean
    AccountService accountService;

    @MockBean
    ImageService imageService;

    @Test
    @WithMockUser
    void getNicknameDuplicate_Docs() throws Exception {
        // given
        BooleanResult duplicateResult = new BooleanResult(false);
        given(memberService.checkNicknameDuplicate("테스트닉네임")).willReturn(duplicateResult);

        // expected
        mockMvc.perform(get("/members/duplicate/nickname")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                        .param("nickname", "테스트닉네임"))
                .andDo(print())
                .andDo(document("member-get-nickname-duplicate",
                        queryParameters(
                                parameterWithName("nickname").description("중복 확인 하려는 닉네임.")
                        ),
                        responseFields(
                                fieldWithPath("result").description("중복 여부. 중복이면 true.")
                        )
                ))
        ;
    }

    @Test
    @WithMockUser
    void signup_Docs() throws Exception {
        // given
        MemberCreate memberCreate = MemberCreate.builder()
                .nickname("연뮤더쿠znzn")
                .build();

        given(memberService.create(any())).willReturn(new IdResult<>(1L));

        // expected
        mockMvc.perform(post("/signup").with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberCreate))
                )
                .andDo(print())
                .andDo(document("member-signup",
                        requestFields(
                                fieldWithPath("nickname").description("회원 닉네임")
                                        .attributes(RestDocsAttribute.constraint("max=15"))
                        ),
                        responseFields(
                                fieldWithPath("id").description("생성된 회원 ID (memberId)")
                        )
                ))
        ;
        then(accountService).should().signupMember(anyString(), eq(1L));
    }

    @Test
    @WithMockUser
    void getMemberDetail_Docs() throws Exception {
        // given
        MemberDetailResponse response = MemberDetailResponse.builder()
                .id(10L)
                .nickname("고라파덕")
                .imageUrl(null)
                .recruitingNum(5L)
                .participationNum(10L)
                .build();
        given(memberService.getDetail(any())).willReturn(response);

        // expected
        mockMvc.perform(get("/members/{memberId}", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-get-member-detail",
                        pathParameters(
                                parameterWithName("memberId").description("회원 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("회원 ID"),
                                fieldWithPath("nickname").description("닉네임")
                                        .attributes(RestDocsAttribute.constraint("max=15")),
                                fieldWithPath("imageId").description("회원 이미지 ID").optional(),
                                fieldWithPath("imageUrl").description("회원 이미지").optional(),
                                fieldWithPath("recruitingNum").description("My 모집"),
                                fieldWithPath("participationNum").description("My 참여")
                        )
                ))
        ;

    }

    @Test
    @WithMockUser
    void editMember_Docs() throws Exception {
        // given
        MemberEdit memberEdit = MemberEdit.builder()
                .nickname("수정이닉네임")
                .imageId(null)
                .build();

        given(accountService.getMemberId(any())).willReturn(5L);

        given(imageService.isOwnedByMember(any(), any())).willReturn(true);

        // expected
        mockMvc.perform(patch("/member")
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberEdit))
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-edit-member",
                        requestFields(
                                fieldWithPath("nickname").description("닉네임")
                                        .attributes(RestDocsAttribute.constraint("max=15")),
                                fieldWithPath("imageId").description("이미지 ID").optional()
                        )
                ));
    }

    @Test
    @WithMockUser
    void getRecruitmentList_Docs() throws Exception {
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
        given(memberService.getRecruitmentList(any(), any(), any()))
                .willReturn(new SliceImpl<>(List.of(partyResponse)));

        // expected
        mockMvc.perform(get("/members/{memberId}/recruitments", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "20")
                        .param("category", PartyCategory.WATCHING.name())
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-get-recruitment-list",
                        pathParameters(
                                parameterWithName("memberId").description("회원 ID")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("페이지 사이즈").optional(),
                                parameterWithName("category").description("카테고리")
                                        .attributes(RestDocsAttribute.type(PartyCategory.class.getSimpleName()))
                                        .optional()
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
    void getParticipationList_Docs() throws Exception {
        // given
        PartyResponse partyResponse = PartyResponse.builder()
                .id(10L)
                .title("공연 같이 보실분~")
                .curMemberNum(2)
                .maxMemberNum(5)
                .showAt(LocalDateTime.of(2023, 4, 28, 19, 30))
                .createdAt(LocalDateTime.of(2023, 4, 28, 11, 12, 28))
                .category(PartyCategory.WATCHING)
                .creatorId(4L)
                .creatorNickname("고라파덕")
                .creatorImageUrl("creator-image-url")
                .showId("PF220846")
                .showName("잘자요, 엄마 [청주]")
                .showPoster("post-image-url")
                .facilityId("FC000182")
                .facilityName("예술나눔 터 (예술나눔 터)")
                .build();
        given(memberService.getParticipationList(any(), any(), any()))
                .willReturn(new SliceImpl<>(List.of(partyResponse)));

        // expected
        mockMvc.perform(get("/members/{memberId}/participations", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "20")
                        .param("category", PartyCategory.WATCHING.name())
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-get-participation-list",
                        pathParameters(
                                parameterWithName("memberId").description("회원 ID")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("페이지 사이즈").optional(),
                                parameterWithName("category").description("카테고리").optional()
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
    void deleteMember_Docs() throws Exception {
        // given
        var memberDelete = MemberDelete.builder()
                .reason(MemberDeleteReason.RECORD_DELETION)
                .content("")
                .build();

        given(accountService.getMemberId(any())).willReturn(5L);

        // expected
        mockMvc.perform(delete("/member")
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberDelete))
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-delete-member",
                        requestFields(
                                fieldWithPath("reason").type(MemberDeleteReason.class.getSimpleName())
                                        .description("회원탈퇴 사유"),
                                fieldWithPath("content").description("내용")
                        )
                ));
    }
}