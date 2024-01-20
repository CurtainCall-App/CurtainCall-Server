package org.cmc.curtaincall.web.lostitem;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.lostitem.LostItem;
import org.cmc.curtaincall.domain.lostitem.LostItemHelper;
import org.cmc.curtaincall.domain.lostitem.LostItemId;
import org.cmc.curtaincall.domain.lostitem.repository.LostItemRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LostItemDeleteService {

    private final LostItemRepository lostItemRepository;

    public void delete(final LostItemId id) {
        LostItem lostItem = LostItemHelper.get(id, lostItemRepository);
        lostItem.getImage().delete();
        lostItemRepository.delete(lostItem);
    }
}
