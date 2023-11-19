package org.cmc.curtaincall.web.member;

import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.cmc.curtaincall.web.common.RestDocsAttribute;
import org.cmc.curtaincall.web.member.request.MemberEdit;
import org.cmc.curtaincall.web.service.image.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(MemberController.class)
class MemberControllerDocsTest extends AbstractWebTest {

    @MockBean
    private MemberService memberService;

    @MockBean
    private ImageService imageService;

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

}