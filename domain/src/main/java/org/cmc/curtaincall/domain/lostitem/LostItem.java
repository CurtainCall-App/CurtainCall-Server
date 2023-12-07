package org.cmc.curtaincall.domain.lostitem;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseEntity;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.image.Image;
import org.cmc.curtaincall.domain.show.FacilityId;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "lost_item",
        indexes = {
                @Index(name = "IX_lost_item__found_date_found_time",
                        columnList = "found_date desc, found_time desc"),
                @Index(name = "IX_lost_item__facility_found_date_found_time",
                        columnList = "facility_id, found_date desc, found_time desc"),
                @Index(name = "IX_lost_item__facility_type_found_date_found_time",
                        columnList = "facility_id, type, found_date desc, found_time desc"),
                @Index(name = "IX_lost_item__found_date_title", columnList = "found_date, title"),
                @Index(name = "IX_lost_item__facility_found_date_title", columnList = "facility_id, found_date, title"),
                @Index(name = "IX_lost_item__facility_type_found_date_title",
                        columnList = "facility_id, type, found_date, title"),
                @Index(name = "IX_lost_item__created_by_created_at", columnList = "created_by, created_at desc")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LostItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lost_item_id")
    private Long id;

    @Embedded
    private FacilityId facilityId;

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

    @Column(name = "found_date", nullable = false)
    private LocalDate foundDate;

    @Column(name = "found_time")
    private LocalTime foundTime;

    @Column(name = "particulars", nullable = false)
    private String particulars;

    @Builder
    public LostItem(
            final FacilityId facilityId,
            final Image image,
            final String title,
            final LostItemType type,
            final String foundPlaceDetail,
            final LocalDate foundDate,
            final @Nullable LocalTime foundTime,
            final String particulars,
            final CreatorId createdBy
    ) {
        this.facilityId = facilityId;
        this.image = image;
        this.title = title;
        this.type = type;
        this.foundPlaceDetail = foundPlaceDetail;
        this.foundDate = foundDate;
        this.foundTime = foundTime;
        this.particulars = particulars;
        this.createdBy = createdBy;
    }

    public LostItemEditor.LostItemEditorBuilder toEditor() {
        return LostItemEditor.builder()
                .image(image)
                .title(title)
                .type(type)
                .foundPlaceDetail(foundPlaceDetail)
                .foundDate(foundDate)
                .foundTime(foundTime)
                .particulars(particulars);
    }

    public void edit(LostItemEditor editor) {
        if (!Objects.equals(image.getId(), editor.image().getId())) {
            image.delete();
        }

        image = editor.image();
        title = editor.title();
        type = editor.type();
        foundPlaceDetail = editor.foundPlaceDetail();
        foundDate = editor.foundDate();
        foundTime = editor.foundTime();
        particulars = editor.particulars();
    }
}
