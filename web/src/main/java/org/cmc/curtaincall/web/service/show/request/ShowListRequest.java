package org.cmc.curtaincall.web.service.show.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.cmc.curtaincall.domain.show.ShowGenre;

@Getter
@Builder
@AllArgsConstructor
public class ShowListRequest {

    @NotNull
    private ShowGenre genre;

}
