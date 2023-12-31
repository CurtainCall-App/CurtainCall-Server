package org.cmc.curtaincall.domain.show;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseTimeEntity;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "facility",
        indexes = @Index(name = "IX_facility__name", columnList = "name")
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Facility extends BaseTimeEntity implements Persistable<FacilityId> {

    @EmbeddedId
    private FacilityId id;

    @Column(name = "name", length = 105, nullable = false)
    private String name;

    @Column(name = "hall_num", nullable = false)
    private Integer hallNum;

    @Column(name = "characteristics", nullable = false)
    private String characteristics;

    @Column(name = "opening_year")
    private Integer openingYear;

    @Column(name = "seat_num", nullable = false)
    private Integer seatNum;

    @Column(name = "phone", length = 45, nullable = false)
    private String phone;

    @Column(name = "homepage", nullable = false)
    private String homepage;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "sido", length = 25, nullable = false)
    private String sido;

    @Column(name = "gugun", length = 25, nullable = false)
    private String gugun;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    public Facility(final FacilityId id) {
        this.id = id;
    }

    @Builder
    private Facility(
            final FacilityId id,
            final String name,
            final Integer hallNum,
            final String characteristics,
            final Integer openingYear,
            final Integer seatNum,
            final String phone,
            final String homepage,
            final String address,
            final String sido,
            final String gugun,
            final Double latitude,
            final Double longitude) {
        this.id = id;
        this.name = name;
        this.hallNum = hallNum;
        this.characteristics = characteristics;
        this.openingYear = openingYear;
        this.seatNum = seatNum;
        this.phone = phone;
        this.homepage = homepage;
        this.address = address;
        this.sido = sido;
        this.gugun = gugun;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }
}
