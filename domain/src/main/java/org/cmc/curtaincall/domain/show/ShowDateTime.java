package org.cmc.curtaincall.domain.show;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;

@Entity
@Table(name = "show_date_time",
        indexes = {
                @Index(name = "IX_show_date_time__show_show_at", columnList = "show_id, show_at"),
                @Index(name = "IX_show_date_time__show_at", columnList = "show_at"),
                @Index(name = "IX_show_date_time__show_end_at", columnList = "show_end_at"),
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

    @Column(name = "show_end_at", nullable = false)
    private LocalDateTime showEndAt;

    public ShowDateTime(Show show, LocalDateTime showAt) {
        this.show = show;
        this.showAt = showAt;
        this.showEndAt = showAt;
        String runtime = show.getRuntime();
        Arrays.stream(runtime.split("\\s+"))
                .forEach(rt -> {
                    if (rt.endsWith("시간")) {
                        long hour = Long.parseLong(rt.replace("시간", ""));
                        this.showEndAt = this.showEndAt.plusHours(hour);
                    } else if (rt.endsWith("분")) {
                        long minute = Long.parseLong(rt.replace("분", ""));
                        this.showEndAt = this.showEndAt.plusMinutes(minute);
                    }
                });
    }
}
