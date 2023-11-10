package org.cmc.curtaincall.domain.member;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseTimeEntity;
import org.cmc.curtaincall.domain.image.Image;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Image image;

    @Builder
    public Member(
            String nickname,
            @Nullable Image image) {
        this.nickname = nickname;
        this.image = image;
    }

    public MemberEditor.MemberEditorBuilder toEditor() {
        return MemberEditor.builder()
                .image(image)
                .nickname(nickname);
    }

    public void edit(MemberEditor editor) {
        nickname = editor.getNickname();
        image = editor.getImage();
    }
}
