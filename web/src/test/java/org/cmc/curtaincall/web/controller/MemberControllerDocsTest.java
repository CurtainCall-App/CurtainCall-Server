package org.cmc.curtaincall.web.controller;

import org.cmc.curtaincall.domain.lostitem.LostItemType;
import org.cmc.curtaincall.domain.member.MemberDeleteReason;
import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.cmc.curtaincall.web.common.RestDocsAttribute;
import org.cmc.curtaincall.web.common.response.BooleanResult;
import org.cmc.curtaincall.web.common.response.IdResult;
import org.cmc.curtaincall.web.review.ShowReviewService;
import org.cmc.curtaincall.web.security.AccountService;
import org.cmc.curtaincall.web.service.image.ImageService;
import org.cmc.curtaincall.web.service.lostitem.LostItemService;
import org.cmc.curtaincall.web.service.lostitem.response.LostItemMyResponse;
import org.cmc.curtaincall.web.service.member.MemberService;
import org.cmc.curtaincall.web.service.member.request.MemberCreate;
import org.cmc.curtaincall.web.service.member.request.MemberDelete;
import org.cmc.curtaincall.web.service.member.request.MemberEdit;
import org.cmc.curtaincall.web.service.member.response.MemberDetailResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(MemberController.class)
class MemberControllerDocsTest extends AbstractWebTest {

    @MockBean
    private MemberService memberService;

    @MockBean
    private AccountService accountService;

    @MockBean
    private ImageService imageService;

    @MockBean
    private ShowReviewService showReviewService;

    @MockBean
    private LostItemService lostItemService;

    @Test
    void getNicknameDuplicate_Docs() throws Exception {
        // given
        BooleanResult duplicateResult = new BooleanResult(false);
        given(memberService.checkNicknameDuplicate("테스트닉네임")).willReturn(duplicateResult);

        // expected
        mockMvc.perform(get("/members/duplicate/nickname")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .param("nickname", "테스트닉네임"))
                .andDo(print())
                .andDo(document("member-get-nickname-duplicate",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
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
    void signup_Docs() throws Exception {
        // given
        MemberCreate memberCreate = MemberCreate.builder()
                .nickname("연뮤더쿠znzn")
                .build();

        given(memberService.create(any())).willReturn(new IdResult<>(1L));

        // expected
        mockMvc.perform(post("/signup")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberCreate))
                )
                .andDo(print())
                .andDo(document("member-signup",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        requestFields(
                                fieldWithPath("nickname").description("회원 닉네임")
                                        .attributes(RestDocsAttribute.constraint("min = 2, max = 15"))
                        ),
                        responseFields(
                                fieldWithPath("id").description("생성된 회원 ID (memberId)")
                        )
                ))
        ;
        then(accountService).should().signupMember(anyString(), eq(1L));
    }

    @Test
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
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-get-member-detail",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
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
    void editMember_Docs() throws Exception {
        // given
        MemberEdit memberEdit = MemberEdit.builder()
                .nickname("수정이닉네임")
                .imageId(null)
                .build();

        given(imageService.isOwnedByMember(any(), any())).willReturn(true);

        // expected
        mockMvc.perform(patch("/member")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberEdit))
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-edit-member",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        requestFields(
                                fieldWithPath("nickname").description("닉네임")
                                        .attributes(RestDocsAttribute.constraint("min = 2, max = 15")),
                                fieldWithPath("imageId").description("이미지 ID").optional()
                        )
                ));
    }

    @Test
    void getMyLostItemList_Docs() throws Exception {
        // given
        var responseList = List.of(
                LostItemMyResponse.builder()
                        .id(10L)
                        .facilityId("FC001298")
                        .facilityName("시온아트홀 (구. JK아트홀, 샘아트홀)")
                        .type(LostItemType.ELECTRONIC_EQUIPMENT)
                        .title("아이패드 핑크")
                        .foundDate(LocalDate.of(2023, 3, 4))
                        .foundTime(LocalTime.of(11, 23))
                        .imageUrl("image-url")
                        .createdAt(LocalDateTime.of(2023, 8, 31, 10, 50))
                        .build()
        );
        given(lostItemService.getMyList(any(), any()))
                .willReturn(new SliceImpl<>(responseList));

        // expected
        mockMvc.perform(get("/member/lostItems")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "20")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-get-my-lost-item-list",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 사이즈").optional()
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("id").description("공연 아이디"),
                                fieldWithPath("facilityId").description("공연시설 ID"),
                                fieldWithPath("facilityName").description("공연시설 이름"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("foundDate").description("습득일자"),
                                fieldWithPath("foundTime").description("습득시간").optional(),
                                fieldWithPath("type").description("분실물 타입")
                                        .type(LostItemType.class.getSimpleName()),
                                fieldWithPath("imageUrl").description("이미지"),
                                fieldWithPath("createdAt").description("생성일시")
                        )
                ));
    }

    @Test
    void deleteMember_Docs() throws Exception {
        // given
        var memberDelete = MemberDelete.builder()
                .reason(MemberDeleteReason.RECORD_DELETION)
                .content("")
                .build();

        // expected
        mockMvc.perform(delete("/member")
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberDelete))
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-delete-member",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        requestFields(
                                fieldWithPath("reason").type(MemberDeleteReason.class.getSimpleName())
                                        .description("회원탈퇴 사유"),
                                fieldWithPath("content").description("내용")
                        )
                ));
    }
}