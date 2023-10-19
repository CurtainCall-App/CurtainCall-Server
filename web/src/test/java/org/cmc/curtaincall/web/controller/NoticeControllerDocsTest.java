package org.cmc.curtaincall.web.controller;

import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.cmc.curtaincall.web.service.notice.NoticeService;
import org.cmc.curtaincall.web.service.notice.response.NoticeDetailResponse;
import org.cmc.curtaincall.web.service.notice.response.NoticeResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoticeController.class)
class NoticeControllerDocsTest extends AbstractWebTest {

    @MockBean
    private NoticeService noticeService;

    @Test
    @WithMockUser
    void getNoticeList_Docs() throws Exception {
        // given
        NoticeResponse noticeResponse = NoticeResponse.builder()
                .id(1L)
                .title("커튼콜 서비스 개편 안내")
                .createdAt(LocalDateTime.of(2023, 6, 17, 1, 23))
                .build();
        given(noticeService.getList(any())).willReturn(new SliceImpl<>(List.of(noticeResponse)));

        // expected
        mockMvc.perform(get("/notices")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .param("page", "0")
                                .param("size", "20")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("notice-get-notice-list",
                        queryParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("페이지 사이즈").optional()
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("`content`"),
                                fieldWithPath("id").description("공지사항 ID"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("createdAt").description("생성 일시")
                        )
                ));
    }

    @Test
    @WithMockUser
    void getNoticeDetail_Docs() throws Exception {
        // given
        NoticeDetailResponse noticeResponse = NoticeDetailResponse.builder()
                .id(1L)
                .title("커튼콜 서비스 개편 안내")
                .content("""
                        안녕하세요, 고객님
                        연극과 뮤지컬 이ㅏㄹ니ㅏㅇㄹ하는 커튼콜입니다.
                                                
                        문의해 주신 내용에 대해서 현재니아ㅓ리나
                        아울러, 니ㅏ어리ㅏ넝ㄹㄴㅇㄹ 안내해 드릴 수 있도록 하겠습니다.
                                                
                        추후 이용 중 다른 문의 사항이 발생한다면 고객센터로 말씀 주시기 바랍니다.
                                                
                        감사합니다.
                        커튼콜팀 드림
                        """
                )
                .createdAt(LocalDateTime.of(2023, 6, 17, 1, 23))
                .build();
        given(noticeService.getDetail(any())).willReturn(noticeResponse);

        // expected
        mockMvc.perform(get("/notices/{noticeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("notice-get-notice-detail",
                        pathParameters(
                                parameterWithName("noticeId").description("공지사항 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("공지사항 ID"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("createdAt").description("생성 일시")
                        )
                ));
    }
}