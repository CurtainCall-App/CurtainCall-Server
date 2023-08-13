package org.cmc.curtaincall.domain.notice;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseTimeEntity;

@Entity
@Table(name = "notice",
        indexes = @Index(name = "IX_notice__created_at", columnList = "created_at desc")
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", length = 4000, nullable = false)
    private String content;

}
