package org.cmc.curtaincall.domain.party;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class PartyId implements Serializable {

    @Column(name = "party_id", nullable = false)
    private Long id;

    public PartyId(final Long id) {
        this.id = Objects.requireNonNull(id);
    }
}
