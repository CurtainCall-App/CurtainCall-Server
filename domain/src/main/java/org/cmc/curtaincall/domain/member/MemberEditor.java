package org.cmc.curtaincall.domain.member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberEditor {

    private String nickname;

    private String introduction;

    @Builder
    private MemberEditor(String nickname, String introduction) {
        this.nickname = nickname;
        this.introduction = introduction;
    }
}
