package org.cmc.curtaincall.web.show.response;

import org.cmc.curtaincall.domain.show.ShowId;

public record ShowFavoriteResponse(
        ShowId showId,
        boolean favorite
) {
}
