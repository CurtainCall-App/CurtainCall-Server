package org.cmc.curtaincall.domain.lostitem.repository;

import org.cmc.curtaincall.domain.lostitem.LostItem;
import org.cmc.curtaincall.domain.lostitem.request.LostItemQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface LostItemQueryRepository {

    Slice<LostItem> query(Pageable pageable, LostItemQueryParam queryParam);
}
