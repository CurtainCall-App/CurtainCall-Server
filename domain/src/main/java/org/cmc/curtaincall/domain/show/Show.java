package org.cmc.curtaincall.domain.show;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseTimeEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
                @Index(name = "IX_show__genre_state_name", columnList = "genre, state, name"),
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Show extends BaseTimeEntity {

    @EmbeddedId
    private ShowId id;

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

    @Column(name = "casts", nullable = false)
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

    @ElementCollection
    @CollectionTable(
            name = "show_time",
            joinColumns = @JoinColumn(name = "show_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    )
    private List<ShowTime> showTimes;

    @ElementCollection
    @CollectionTable(
            name = "shows_introduction_images",
            joinColumns = @JoinColumn(name = "show_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    )
    @Column(name = "image_url", length = 500, nullable = false)
    private List<String> introductionImages = new ArrayList<>();

    @Builder
    public Show(
            final ShowId id,
            final Facility facility,
            final String name,
            final LocalDate startDate,
            final LocalDate endDate,
            final String crew,
            final String cast,
            final String runtime,
            final String age,
            final String enterprise,
            final String ticketPrice,
            final String poster,
            final String story,
            final ShowGenre genre,
            final ShowState state,
            final String openRun,
            final List<ShowTime> showTimes,
            final List<String> introductionImages
    ) {
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

    public List<ShowTime> getShowTimes() {
        return Collections.unmodifiableList(showTimes);
    }

    public List<String> getIntroductionImages() {
        return Collections.unmodifiableList(introductionImages);
    }
}
