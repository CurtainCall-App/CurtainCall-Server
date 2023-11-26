package org.cmc.curtaincall.domain.lostitem;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class LostItemId implements Serializable {

    @Column(name = "lost_item_id", nullable = false)
    private Long id;

    public LostItemId(final Long id) {
        this.id = id;
    }
}
