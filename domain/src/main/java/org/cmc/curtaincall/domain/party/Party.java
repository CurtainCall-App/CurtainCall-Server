package org.cmc.curtaincall.domain.party;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseEntity;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.party.exception.PartyAlreadyClosedException;
import org.cmc.curtaincall.domain.party.exception.PartyAlreadyParticipatedException;
import org.cmc.curtaincall.domain.show.ShowId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "party",
        indexes = {
                @Index(name = "IX_party__category_created_at", columnList = "category, created_at desc"),
                @Index(name = "IX_party__show_category_created_at", columnList = "show_id, category, created_at desc"),
                @Index(name = "IX_party__created_by_created_at", columnList = "created_by, created_at desc"),
                @Index(name = "IX_party__created_by_category_created_at",
                        columnList = "created_by, category, created_at desc")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Party extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id")
    private Long id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "show_id"))
    private ShowId showId;

    @Column(name = "party_at")
    private LocalDateTime partyAt;

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
    private List<PartyMember> partyMembers = new ArrayList<>();

    @Builder
    public Party(
            ShowId showId,
            LocalDateTime partyAt,
            String title,
            String content,
            Integer maxMemberNum,
            PartyCategory category) {
        this.showId = showId;
        this.partyAt = partyAt;
        this.title = title;
        this.content = content;
        this.maxMemberNum = maxMemberNum;
        this.category = category;

        if (category == PartyCategory.ETC) {
            this.showId = null;
            this.partyAt = null;
        }
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

    public void open() {
        closed = false;
    }

    public void participate(final MemberId memberId) {
        if (Boolean.TRUE.equals(closed) || !partyAt.isBefore(LocalDateTime.now())) {
            throw new PartyAlreadyClosedException(new PartyId(id));
        }
        if (isParticipated(memberId)) {
            throw new PartyAlreadyParticipatedException(new PartyId(id), memberId);
        }
        partyMembers.add(new PartyMember(this, memberId));
        curMemberNum += 1;

        if (curMemberNum.intValue() == maxMemberNum.intValue()) {
            close();
        }
    }

    public void cancelParticipate(final MemberId memberId) {
        partyMembers = partyMembers.stream()
                .filter(partyMember -> !partyMember.getMemberId().equals(memberId))
                .toList();

        curMemberNum -= 1;
        open();
    }

    public boolean isParticipated(final MemberId memberId) {
        boolean isCreator = Objects.equals(getCreatedBy().getMemberId(), memberId);
        boolean isParticipant = getPartyMembers().stream()
                .anyMatch(partyMember -> Objects.equals(partyMember.getMemberId(), memberId));
        return isCreator || isParticipant;
    }

    public List<PartyMember> getPartyMembers() {
        return Collections.unmodifiableList(this.partyMembers);
    }

}
