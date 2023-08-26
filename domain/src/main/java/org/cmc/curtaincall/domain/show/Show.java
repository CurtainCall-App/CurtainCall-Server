package org.cmc.curtaincall.domain.show;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseTimeEntity;
import org.cmc.curtaincall.domain.review.ShowReview;
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
                @Index(name = "IX_show__genre_end_date", columnList = "genre, end_date"),
                @Index(name = "IX_show__genre_name", columnList = "genre, name"),
                @Index(name = "IX_show__genre_review_grade_avg", columnList = "genre, review_grade_avg desc"),
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

    @Column(name = "story", length = 4000, nullable = false)
    private String story;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre", length = 25, nullable = false)
    private ShowGenre genre;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", length = 25, nullable = false)
    private ShowState state;

    @Column(name = "openrun", length = 25, nullable = false)
    private String openRun;

    @Column(name = "review_count", nullable = false)
    private Integer reviewCount = 0;

    @Column(name = "review_grade_sum", nullable = false)
    private Long reviewGradeSum = 0L;

    @Column(name = "review_grade_avg", nullable = false)
    private Double reviewGradeAvg = 0D;

    @ElementCollection
    @CollectionTable(
            name = "show_time",
            joinColumns = @JoinColumn(name = "show_id", foreignKey = @ForeignKey(name = "FK_show_time"))
    )
    private List<ShowTime> showTimes;

    @ElementCollection
    @CollectionTable(
            name = "shows_introduction_images",
            joinColumns = @JoinColumn(name = "show_id", foreignKey = @ForeignKey(name = "FK_shows_introduction_images"))
    )
    private List<String> introductionImages;

    @Builder
    private Show(
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
            ShowState state,
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
    }

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }

    public void applyReview(ShowReview review) {
        reviewCount += 1;
        reviewGradeSum += review.getGrade();
        calculateReviewGradeAvg();
    }

    public void cancelReview(ShowReview review) {
        reviewCount -= 1;
        reviewGradeSum -= review.getGrade();
        calculateReviewGradeAvg();
    }

    private void calculateReviewGradeAvg() {
        reviewGradeAvg = ((double) reviewGradeSum) / reviewCount;
    }
}
