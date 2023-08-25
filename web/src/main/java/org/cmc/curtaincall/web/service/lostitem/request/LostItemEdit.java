package org.cmc.curtaincall.web.service.lostitem.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.cmc.curtaincall.domain.lostitem.LostItemType;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LostItemEdit {

    @NotBlank
    @Size(max = 20)
    private String title;

    @NotNull
    private LostItemType type;

    @NotNull
    @Size(max = 30)
    private String foundPlaceDetail;

    @NotNull
    private LocalDate foundDate;

    private LocalTime foundTime;

    @NotBlank
    @Size(max = 100)
    private String particulars;

    @NotNull
    private Long imageId;
}
