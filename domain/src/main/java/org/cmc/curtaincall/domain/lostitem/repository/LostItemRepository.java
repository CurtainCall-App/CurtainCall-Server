package org.cmc.curtaincall.domain.lostitem.repository;

import org.cmc.curtaincall.domain.lostitem.LostItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LostItemRepository extends JpaRepository<LostItem, Long> {
}
