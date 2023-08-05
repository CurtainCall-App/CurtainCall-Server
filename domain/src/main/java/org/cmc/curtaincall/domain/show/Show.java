package org.cmc.curtaincall.domain.show;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseTimeEntity;
import org.springframework.data.domain.Persistable;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "shows",
        indexes = {
                @Index(name = "IX_show__facility", columnList = "facility_id"),
                @Index(name = "IX_show__name", columnList = "name"),
                @Index(name = "IX_show__start_date", columnList = "start_date"),
                @Index(name = "IX_show__end_date", columnList = "end_date"),
                @Index(name = "IX_show__genre", columnList = "genre"),
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Show extends BaseTimeEntity implements Persistable<String> {

    @Id
    @Column(name = "show_id", length = 25)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "facility_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Facility facility;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "crew", nullable = false)
    private String crew;

    @Column(name = "cast", nullable = false)
    private String cast;

    @Column(name = "runtime", nullable = false)
    private String runtime;

    @Column(name = "age", nullable = false)
    private String age;

    @Column(name = "enterprise", nullable = false)
    private String enterprise;

    @Column(name = "ticket_price", nullable = false)
    private String ticketPrice;

    @Column(name = "poster", nullable = false)
    private String poster;

    @Column(name = "story", length = 1000, nullable = false)
    private String story;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre", length = 25, nullable = false)
    private ShowGenre genre;

    @Column(name = "state", length = 25, nullable = false)
    private String state;

    @Column(name = "openrun", length = 25, nullable = false)
    private String openRun;

    @Column(name = "review_count", nullable = false)
    private Integer reviewCount;

    @Column(name = "review_grade_sum", nullable = false)
    private Long reviewGradeSum;

    @ElementCollection
    @CollectionTable(name = "show_time", joinColumns = @JoinColumn(name = "show_id"))
    private List<ShowTime> showTimes;

    @ElementCollection
    @CollectionTable(name = "shows_introduction_images", joinColumns = @JoinColumn(name = "show_id"))
    private List<String> introductionImages;

    public Show(
            String id,
            Facility facility,
            String name,
            LocalDate startDate,
            LocalDate endDate,
            String crew,
            String cast,
            String runtime,
            String age,
            String enterprise,
            String ticketPrice,
            String poster,
            String story,
            ShowGenre genre,
            String state,
            String openRun,
            List<ShowTime> showTimes,
            List<String> introductionImages) {
        this.id = id;
        this.facility = facility;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.crew = crew;
        this.cast = cast;
        this.runtime = runtime;
        this.age = age;
        this.enterprise = enterprise;
        this.ticketPrice = ticketPrice;
        this.poster = poster;
        this.story = story;
        this.genre = genre;
        this.state = state;
        this.openRun = openRun;
        this.showTimes = showTimes;
        this.introductionImages = introductionImages;
        this.reviewCount = 0;
        this.reviewGradeSum = 0L;
    }

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }
}