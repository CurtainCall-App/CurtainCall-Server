package org.cmc.curtaincall.web.show.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShowFavoriteResponse {

    private String showId;

    private boolean favorite;

    public ShowFavoriteResponse(String showId, boolean favorite) {
        this.showId = showId;
        this.favorite = favorite;
    }
}
