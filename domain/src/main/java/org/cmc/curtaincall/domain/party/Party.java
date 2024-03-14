package org.cmc.curtaincall.domain.party;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseEntity;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.party.exception.PartyAlreadyClosedException;
import org.cmc.curtaincall.domain.party.exception.PartyAlreadyParticipatedException;
import org.cmc.curtaincall.domain.party.exception.PartyRecruiterNotAllowedLeaveException;
import org.cmc.curtaincall.domain.show.ShowId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "party",
        indexes = {
                @Index(name = "IX_party__created_at", columnList = "created_at desc"),
                @Index(name = "IX_party__created_by_created_at", columnList = "created_by, created_at desc"),
                @Index(name = "IX_party__party_at", columnList = "party_at"),
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

    @Column(name = "party_at", nullable = false)
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

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PartyMember> partyMembers = new ArrayList<>();

    @Builder
    public Party(
            final ShowId showId,
            final LocalDateTime partyAt,
            final String title,
            final String content,
            final Integer maxMemberNum,
            final CreatorId createdBy
    ) {
        this.showId = showId;
        this.partyAt = partyAt;
        this.title = title;
        this.content = content;
        this.maxMemberNum = maxMemberNum;
        this.createdBy = createdBy;
        this.partyMembers.add(new PartyMember(this, PartyMemberRole.RECRUITER, createdBy.getMemberId()));
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
        partyMembers.add(new PartyMember(this, PartyMemberRole.PARTICIPANT, memberId));
        curMemberNum += 1;

        if (curMemberNum.intValue() == maxMemberNum.intValue()) {
            close();
        }
    }

    public void leave(final MemberId memberId) {
        final PartyMember partyMember = partyMembers
                .stream()
                .filter(pm -> pm.getMemberId().equals(memberId))
                .findAny()
                .orElseThrow(() -> new PartyAlreadyParticipatedException(new PartyId(id), memberId));
        if (partyMember.getRole() == PartyMemberRole.RECRUITER) {
            throw new PartyRecruiterNotAllowedLeaveException(new PartyId(id), memberId);
        }
        partyMembers.remove(partyMember);
        curMemberNum -= 1;
        open();
    }

    public boolean isParticipated(final MemberId memberId) {
        return getPartyMembers().stream()
                .anyMatch(partyMember -> Objects.equals(partyMember.getMemberId(), memberId));
    }

    public List<PartyMember> getPartyMembers() {
        return Collections.unmodifiableList(this.partyMembers);
    }

}
