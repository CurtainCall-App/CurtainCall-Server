package org.cmc.curtaincall.web.service.party.request;

import jakarta.validation.constraints.*;
import lombok.*;
import org.cmc.curtaincall.domain.party.PartyCategory;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PartyCreate {

    private String showId;

    private LocalDateTime showAt;

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank
    @Size(max = 400)
    private String content;

    @Positive
    @Max(10L)
    private Integer maxMemberNum;

    @NotNull
    private PartyCategory category;
}
