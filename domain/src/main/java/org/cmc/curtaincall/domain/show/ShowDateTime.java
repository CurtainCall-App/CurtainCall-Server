package org.cmc.curtaincall.domain.show;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "show_date_time",
        indexes = {
                @Index(name = "IX_show_date_time__show_show_at", columnList = "show_id, show_at"),
                @Index(name = "IX_show_date_time__show_at", columnList = "show_at"),
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShowDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "show_date_time_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "show_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Show show;

    @Column(name = "show_at", nullable = false)
    private LocalDateTime showAt;

    public ShowDateTime(Show show, LocalDateTime showAt) {
        this.show = show;
        this.showAt = showAt;
    }
}
