package org.cmc.curtaincall.domain.party;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "party",
        indexes = {
                @Index(name = "IX_party__mt20id", columnList = "mt20id"),
                @Index(name = "IX_party__created_by", columnList = "created_by")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Party extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id")
    private Long id;

    @Column(name = "mt20id", length = 25, nullable = false)
    private String showId;

    @Column(name = "show_name", nullable = false)
    private String showName;

    @Column(name = "show_at", nullable = false)
    private LocalDateTime showAt;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", length = 2000, nullable = false)
    private String content;

    @Column(name = "cur_member_num", nullable = false)
    private Integer curMemberNum;

    @Column(name = "max_member_num", nullable = false)
    private Integer maxMemberNum;

    @Column(name = "closed", nullable = false)
    private Boolean closed = false;

    public Party(
            String showId,
            String showName,
            LocalDateTime showAt,
            String title,
            String content,
            Integer curMemberNum,
            Integer maxMemberNum) {
        this.showId = showId;
        this.showName = showName;
        this.showAt = showAt;
        this.title = title;
        this.content = content;
        this.curMemberNum = curMemberNum;
        this.maxMemberNum = maxMemberNum;
    }

    public PartyEditor.PartyEditorBuilder toEditor() {
        return PartyEditor.builder()
                .title(title)
                .content(content);
    }

    public void edit(PartyEditor editor) {
        title = editor.getTitle();
        content = editor.getContent();
    }
}
