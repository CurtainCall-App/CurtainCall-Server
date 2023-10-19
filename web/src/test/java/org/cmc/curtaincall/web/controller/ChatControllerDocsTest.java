package org.cmc.curtaincall.web.controller;

import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.cmc.curtaincall.web.common.response.ValueResult;
import org.cmc.curtaincall.web.service.chat.GetStreamChatService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
class ChatControllerDocsTest extends AbstractWebTest {

    @MockBean
    private GetStreamChatService getStreamChatService;

    @Test
    @WithMockUser
    void getChatToken_Docs() throws Exception {
        // given
        given(getStreamChatService.createToken(anyLong())).willReturn(new ValueResult<>("CHAT_TOKEN"));

        // expected
        mockMvc.perform(get("/chat-token")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("chat-get-chat-token",
                        responseFields(
                                fieldWithPath("value").description("채팅 토큰")
                        )
                ));
    }
}