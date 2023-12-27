package org.cmc.curtaincall.domain.show;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.member.MemberId;

@Entity
@Table(name = "favorite_show",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_favorite_show__member_show", columnNames = {"member_id", "show_id"})
        },
        indexes = {
                @Index(name = "IX_favorite_show__show", columnList = "show_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteShow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_show_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "show_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Show show;

    @Embedded
    private MemberId memberId;

    public FavoriteShow(final Show show, final MemberId memberId) {
        this.show = show;
        this.memberId = memberId;
    }
}
