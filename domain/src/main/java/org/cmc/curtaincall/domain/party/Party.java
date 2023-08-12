package org.cmc.curtaincall.domain.party;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseEntity;
import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.show.Show;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "party",
        indexes = {
                @Index(name = "IX_party__category_created_at", columnList = "category,created_at"),
                @Index(name = "IX_party__show_category_created_at", columnList = "show_id,category,created_at"),
                @Index(name = "IX_party__created_by_category_created_at", columnList = "created_by,category,created_at")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Party extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "show_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Show show;

    @Column(name = "show_at", nullable = false)
    private LocalDateTime showAt;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", length = 2000, nullable = false)
    private String content;

    @Column(name = "cur_member_num", nullable = false)
    private Integer curMemberNum = 1;

    @Column(name = "max_member_num", nullable = false)
    private Integer maxMemberNum;

    @Column(name = "closed", nullable = false)
    private Boolean closed = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 25, nullable = false)
    private PartyCategory category;

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    List<PartyMember> partyMembers = new ArrayList<>();

    @Builder
    public Party(
            Show show,
            LocalDateTime showAt,
            String title,
            String content,
            Integer maxMemberNum,
            PartyCategory category) {
        this.show = show;
        this.showAt = showAt;
        this.title = title;
        this.content = content;
        this.maxMemberNum = maxMemberNum;
        this.category = category;
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

    public void close() {
        closed = true;
    }

    public void participate(Member member) {
        partyMembers.add(new PartyMember(this, member));
        curMemberNum += 1;

        if (curMemberNum.intValue() == maxMemberNum.intValue()) {
            close();
        }
    }

}
