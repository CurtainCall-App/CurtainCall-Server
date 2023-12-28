package org.cmc.curtaincall.web.lostitem.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class LostItemEdit {

    @NotBlank
    @Size(max = 20)
    private String title;

    @NotNull
    @Size(max = 30)
    private String foundPlaceDetail;

    @NotNull
    @PastOrPresent
    private LocalDate foundDate;

    private LocalTime foundTime;

    @NotNull
    @Size(max = 100)
    private String particulars;

    @NotNull
    private Long imageId;
}
