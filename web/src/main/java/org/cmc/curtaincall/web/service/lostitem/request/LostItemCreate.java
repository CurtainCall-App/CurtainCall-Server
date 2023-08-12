package org.cmc.curtaincall.web.service.lostitem.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.cmc.curtaincall.domain.lostitem.LostItemType;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LostItemCreate {

    @NotBlank
    @Size(max = 200)
    private String title;

    @NotNull
    private LostItemType type;

    @NotBlank
    @Size(max = 25)
    private String facilityId;

    @NotBlank
    @Size(max = 200)
    private String foundPlaceDetail;

    @NotNull
    private LocalDateTime foundAt;

    @NotBlank
    @Size(max = 200)
    private String particulars;

    @NotNull
    private Long imageId;
}
