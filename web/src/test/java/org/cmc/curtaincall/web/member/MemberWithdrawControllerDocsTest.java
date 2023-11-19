package org.cmc.curtaincall.web.member;

import org.cmc.curtaincall.domain.member.MemberWithdrawReason;
import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.cmc.curtaincall.web.member.request.MemberWithdraw;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberWithdrawController.class)
class MemberWithdrawControllerDocsTest extends AbstractWebTest {

    @MockBean
    private MemberWithdrawService withdrawService;

    @Test
    void withdraw_Docs() throws Exception {
        // given
        var memberDelete = MemberWithdraw.builder()
                .reason(MemberWithdrawReason.RECORD_DELETION)
                .content("")
                .build();

        // expected
        mockMvc.perform(delete("/member")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberDelete))
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-withdraw",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        requestFields(
                                fieldWithPath("reason").type(MemberWithdrawReason.class.getSimpleName())
                                        .description("회원탈퇴 사유"),
                                fieldWithPath("content").description("내용")
                        )
                ));
    }
}