package org.cmc.curtaincall.domain.show;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class FacilityId implements Serializable {

    @Column(name = "facility_id", length = 25, nullable = false)
    private String id;

    public FacilityId(final String id) {
        this.id = id;
    }
}
