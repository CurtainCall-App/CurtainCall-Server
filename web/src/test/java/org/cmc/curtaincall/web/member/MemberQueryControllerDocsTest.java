package org.cmc.curtaincall.web.member;

import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.member.dao.MemberDao;
import org.cmc.curtaincall.domain.member.response.MemberDetailResponse;
import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.cmc.curtaincall.web.common.RestDocsAttribute;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberQueryController.class)
class MemberQueryControllerDocsTest extends AbstractWebTest {

    @MockBean
    private MemberDao memberDao;

    @Test
    void getDetail_Docs() throws Exception {
        // given
        var response = MemberDetailResponse.builder()
                .id(new MemberId(10L))
                .nickname("고라파덕")
                .imageUrl(null)
                .recruitingNum(5L)
                .participationNum(10L)
                .build();
        given(memberDao.getDetail(new MemberId(10L))).willReturn(response);

        // expected
        mockMvc.perform(get("/members/{memberId}", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-get-detail",
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
}