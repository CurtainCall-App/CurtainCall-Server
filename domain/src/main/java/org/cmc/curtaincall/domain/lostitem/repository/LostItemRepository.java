package org.cmc.curtaincall.domain.lostitem.repository;

import org.cmc.curtaincall.domain.lostitem.LostItem;
import org.cmc.curtaincall.domain.member.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LostItemRepository extends JpaRepository<LostItem, Long> {

    @EntityGraph(attributePaths = {"facility", "image"})
    Slice<LostItem> findSliceByCreatedByAndUseYnIsTrue(Pageable pageable, Member createdBy);
}
