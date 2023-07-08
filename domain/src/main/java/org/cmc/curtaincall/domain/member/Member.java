package org.cmc.curtaincall.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseTimeEntity;

@Entity
@Table(name = "member",
        uniqueConstraints = @UniqueConstraint(name = "UK_member__nickname", columnNames = "nickname")
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "nickname", length = 25, nullable = false)
    private String nickname;

    @Builder
    private Member(
            String nickname) {
        this.nickname = nickname;
    }

    public MemberEditor.MemberEditorBuilder toEditor() {
        return MemberEditor.builder()
                .nickname(nickname);
    }

    public void edit(MemberEditor editor) {
        nickname = editor.getNickname();
    }
}
