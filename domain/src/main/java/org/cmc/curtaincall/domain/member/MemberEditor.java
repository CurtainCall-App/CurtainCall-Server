package org.cmc.curtaincall.domain.member;

import lombok.Builder;
import lombok.Getter;
import org.cmc.curtaincall.domain.image.Image;

@Getter
public class MemberEditor {

    private String nickname;

    private Image image;

    @Builder
    private MemberEditor(String nickname, Image image) {
        this.nickname = nickname;
        this.image = image;
    }
}
