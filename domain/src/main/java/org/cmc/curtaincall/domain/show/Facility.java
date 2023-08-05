package org.cmc.curtaincall.domain.show;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseTimeEntity;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "facility")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Facility extends BaseTimeEntity implements Persistable<String> {

    @Id
    @Column(name = "facility_id", length = 25)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "hall_num", nullable = false)
    private Integer hallNum;

    @Column(name = "characteristics", nullable = false)
    private String characteristics;

    @Column(name = "opening_year", nullable = false)
    private Integer openingYear;

    @Column(name = "seat_num", nullable = false)
    private Integer seatNum;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "homepage", nullable = false)
    private String homepage;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Builder
    public Facility(
            String id,
            String name,
            Integer hallNum,
            String characteristics,
            Integer openingYear,
            Integer seatNum,
            String phone,
            String homepage,
            String address,
            Double latitude,
            Double longitude) {
        this.id = id;
        this.name = name;
        this.hallNum = hallNum;
        this.characteristics = characteristics;
        this.openingYear = openingYear;
        this.seatNum = seatNum;
        this.phone = phone;
        this.homepage = homepage;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }
}