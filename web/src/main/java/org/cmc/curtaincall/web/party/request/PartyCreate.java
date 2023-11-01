package org.cmc.curtaincall.web.party.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.*;
import org.cmc.curtaincall.domain.party.PartyCategory;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PartyCreate {

    @Nullable
    private String showId;

    @Nullable
    private LocalDateTime showAt;

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank
    @Size(max = 400)
    private String content;

    @Min(2L)
    @Max(100L)
    private Integer maxMemberNum;

    @NotNull
    private PartyCategory category;
}