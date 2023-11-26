package org.cmc.curtaincall.domain.core;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.*;
import org.cmc.curtaincall.domain.member.MemberId;

import java.util.Objects;
import java.util.Optional;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class CreatorId {

    @Embedded
    @AttributeOverride(
            name = "id",
            column = @Column(name = "created_by", updatable = false, nullable = false)
    )
    private MemberId memberId;

    public CreatorId(final long id) {
        this.memberId = new MemberId(id);
    }

    public CreatorId(final MemberId memberId) {
        this.memberId = Objects.requireNonNull(memberId);
    }

    public Long getId() {
        return Optional.ofNullable(memberId)
                .map(MemberId::getId)
                .orElse(null);
    }
}
