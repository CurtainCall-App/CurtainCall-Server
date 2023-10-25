package org.cmc.curtaincall.web.chat;

import io.getstream.chat.java.models.User;
import org.cmc.curtaincall.web.common.response.ValueResult;
import org.springframework.stereotype.Service;

@Service
public class GetStreamChatService {

    public ValueResult<String> createToken(long memberId) {
        String token = User.createToken(Long.toString(memberId), null, null);
        return new ValueResult<>(token);
    }
}
