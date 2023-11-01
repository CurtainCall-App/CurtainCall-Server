package org.cmc.curtaincall.domain.show;

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
public class ShowId implements Serializable {

    @Column(name = "show_id", nullable = false)
    private String id;

    public ShowId(String id) {
        this.id = Objects.requireNonNull(id);
    }
}
