package org.cmc.curtaincall.domain.show;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseTimeEntity;

import java.time.LocalDate;

@Entity
@Table(name = "box_office",
        indexes = {
                @Index(name = "IX_box_office__base_date_type_genre_rank_num",
                        columnList = "base_date desc, type, genre, rank_num")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoxOffice extends BaseTimeEntity {

    @Id
    @Column(name = "box_office_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "base_date", nullable = false)
    private LocalDate baseDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 25, nullable = false)
    private BoxOfficeType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre", length = 25, nullable = false)
    private BoxOfficeGenre genre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "show_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Show show;

    @Column(name = "rank_num", nullable = false)
    private Integer rank;

    @Builder
    private BoxOffice(
            LocalDate baseDate,
            BoxOfficeType type,
            BoxOfficeGenre genre,
            Show show,
            Integer rank) {
        this.baseDate = baseDate;
        this.type = type;
        this.genre = genre;
        this.show = show;
        this.rank = rank;
    }
}
