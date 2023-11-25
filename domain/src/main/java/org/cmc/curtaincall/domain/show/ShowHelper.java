package org.cmc.curtaincall.domain.show;

import org.cmc.curtaincall.domain.show.exception.ShowNotFoundException;
import org.cmc.curtaincall.domain.show.repository.ShowRepository;

public final class ShowHelper {

    private ShowHelper() {
        throw new UnsupportedOperationException();
    }

    public static Show get(ShowId showId, ShowRepository showRepository) {
        return showRepository.findById(showId)
                .filter(Show::getUseYn)
                .orElseThrow(() -> new ShowNotFoundException(showId));
    }

    public static Show getWithOptimisticLock(ShowId showId, ShowRepository showRepository) {
        return showRepository.findWithLockById(showId)
                .filter(Show::getUseYn)
                .orElseThrow(() -> new ShowNotFoundException(showId));
    }
}
