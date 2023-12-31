package org.cmc.curtaincall.web.party.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class PartyEdit {

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank
    @Size(max = 400)
    private String content;
}
