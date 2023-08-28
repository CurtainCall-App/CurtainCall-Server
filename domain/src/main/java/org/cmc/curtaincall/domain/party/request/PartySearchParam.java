package org.cmc.curtaincall.domain.party.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.cmc.curtaincall.domain.party.PartyCategory;

@Getter
@Builder
@AllArgsConstructor
public class PartySearchParam {

    @NotNull
    private PartyCategory category;

    @Size(max = 200)
    @NotNull
    private String keyword;

}
