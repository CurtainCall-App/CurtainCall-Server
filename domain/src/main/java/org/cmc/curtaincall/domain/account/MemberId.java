package org.cmc.curtaincall.domain.account;

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
public class MemberId implements Serializable {

    @Column(name = "member_id")
    private Long id;

    public MemberId(Long id) {
        this.id = Objects.requireNonNull(id);
    }
}
