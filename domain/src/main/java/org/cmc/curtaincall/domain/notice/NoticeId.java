package org.cmc.curtaincall.domain.notice;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class NoticeId {

    @Column(name = "notice_id")
    private Long id;

    public NoticeId(final Long id) {
        this.id = Objects.requireNonNull(id);
    }
}
