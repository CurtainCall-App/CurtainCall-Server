package org.cmc.curtaincall.web.show.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowState;

@Getter
@NoArgsConstructor
@Setter
public class ShowListRequest {

    @NotNull
    private ShowGenre genre;

    private ShowState state = ShowState.PERFORMING;

}
