package org.cmc.curtaincall.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class MemberId implements Serializable {

    @Column(name = "member_id", nullable = false)
    private Long id;

    public MemberId(final Long id) {
        this.id = id;
    }
}
