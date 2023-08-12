package org.cmc.curtaincall.domain.lostitem;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseEntity;
import org.cmc.curtaincall.domain.image.Image;
import org.cmc.curtaincall.domain.show.Facility;

import java.time.LocalDateTime;

@Entity
@Table(name = "lost_item",
        indexes = {
                @Index(name = "IX_lost_item__found_at", columnList = "found_at"),
                @Index(name = "IX_lost_item__title_found_at", columnList = "title,found_at"),
                @Index(name = "IX_lost_item__title_facility_found_at", columnList = "title,facility_id,found_at"),
                @Index(name = "IX_lost_item__facility_found_at", columnList = "facility_id,found_at"),
                @Index(name = "IX_lost_item__facility_type_found_at", columnList = "facility_id,type,found_at"),
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LostItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lost_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "facility_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Facility facility;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "image_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Image image;

    @Column(name = "title", nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private LostItemType type;

    @Column(name = "found_place_detail", nullable = false)
    private String foundPlaceDetail;

    @Column(name = "found_at", nullable = false)
    private LocalDateTime foundAt;

    @Column(name = "particulars", nullable = false)
    private String particulars;

    @Builder
    private LostItem(
            Facility facility,
            Image image,
            String title,
            LostItemType type,
            String foundPlaceDetail,
            LocalDateTime foundAt,
            String particulars) {
        this.facility = facility;
        this.image = image;
        this.title = title;
        this.type = type;
        this.foundPlaceDetail = foundPlaceDetail;
        this.foundAt = foundAt;
        this.particulars = particulars;
    }
}
