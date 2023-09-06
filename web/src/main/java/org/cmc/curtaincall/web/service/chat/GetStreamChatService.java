package org.cmc.curtaincall.web.service.chat;

import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.Channel;
import io.getstream.chat.java.models.Language;
import io.getstream.chat.java.models.User;
import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.party.Party;
import org.cmc.curtaincall.web.exception.GetStreamChatException;
import org.cmc.curtaincall.web.service.common.response.ValueResult;
import org.springframework.stereotype.Service;

@Service
public class GetStreamChatService {

    private static final String CHANNEL_TYPE = "messaging";

    public static String getPartyChannelId(Party party) {
        return "PARTY-" + party.getId();
    }

    public void upsertUser(Member member) {
        try {
            User.upsert()
                    .user(User.UserRequestObject.builder()
                            .id(member.getId().toString())
                            .name(member.getNickname())
                            .language(Language.KO)
                            .build()
                    )
                    .request();
        } catch (StreamException e) {
            throw new GetStreamChatException(e);
        }
    }

    public ValueResult<String> createToken(long memberId) {
        String token = User.createToken(Long.toString(memberId), null, null);
        return new ValueResult<>(token);
    }

    public void createPartyChannel(Party party) {
        User.UserRequestObject createdBy = User.UserRequestObject.builder()
                .id(party.getCreatedBy().getId().toString())
                .name(party.getCreatedBy().getNickname())
                .build();
        String partyChannelId = getPartyChannelId(party);
        createChannel(partyChannelId, createdBy);
        addMember(partyChannelId, party.getCreatedBy().getId());
    }

    public void deletePartyChannel(Party party) {
        String partyChannelId = getPartyChannelId(party);
        try {
            Channel.delete(CHANNEL_TYPE, partyChannelId).request();
        } catch (StreamException e) {
            throw new GetStreamChatException(e);
        }
    }

    private void createChannel(String channelId, User.UserRequestObject createdBy) {
        try {
            Channel.getOrCreate(CHANNEL_TYPE, channelId)
                    .data(Channel.ChannelRequestObject.builder()
                            .createdBy(createdBy)
                            .build())
                    .request();
        } catch (StreamException e) {
            throw new GetStreamChatException(e);
        }
    }

    public void addMember(String channelId, long memberId) {
        try {
            Channel.update(CHANNEL_TYPE, channelId).addMember(Long.toString(memberId)).request();
        } catch (StreamException e) {
            throw new GetStreamChatException(e);
        }
    }

    public void removeMember(String channelId, long memberId) {
        try {
            Channel.update(CHANNEL_TYPE, channelId).removeMember(Long.toString(memberId)).request();
        } catch (StreamException e) {
            throw new GetStreamChatException(e);
        }
    }
}
