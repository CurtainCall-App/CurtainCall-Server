package org.cmc.curtaincall.domain.lostitem;

import org.cmc.curtaincall.domain.lostitem.exception.LostItemNotFoundException;
import org.cmc.curtaincall.domain.lostitem.repository.LostItemRepository;

public final class LostItemHelper {

    private LostItemHelper() {
        throw new UnsupportedOperationException();
    }

    public static LostItem get(LostItemId id, LostItemRepository lostItemRepository) {
        return lostItemRepository.findById(id.getId())
                .filter(LostItem::getUseYn)
                .orElseThrow(() -> new LostItemNotFoundException(id));
    }

}
