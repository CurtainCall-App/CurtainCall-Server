package org.cmc.curtaincall.web.controller;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.web.security.annotation.LoginMemberId;
import org.cmc.curtaincall.web.service.chat.GetStreamChatService;
import org.cmc.curtaincall.web.service.common.response.ValueResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final GetStreamChatService getStreamChatService;

    @GetMapping("/chat-token")
    public ValueResult<String> getChatToken(@LoginMemberId Long memberId) {
        return getStreamChatService.createToken(memberId);
    }
}
