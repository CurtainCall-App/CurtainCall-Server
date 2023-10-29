package org.cmc.curtaincall.domain.review;

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
public class ShowReviewId implements Serializable {

    @Column(name = "show_review_id", nullable = false)
    private Long id;

    public ShowReviewId(final Long id) {
        this.id = Objects.requireNonNull(id);
    }
}
