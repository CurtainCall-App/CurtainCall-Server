package org.cmc.curtaincall.domain.party;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PartyEditor {

    private String title;

    private String content;

    @Builder
    public PartyEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
