package org.cmc.curtaincall.domain.party.repository;

import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.party.Party;
import org.cmc.curtaincall.domain.party.PartyCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long> {

    @EntityGraph(attributePaths = {"createdBy", "createdBy.image", "show", "show.facility"})
    Slice<Party> findSliceWithByCategoryAndUseYnIsTrue(Pageable pageable, PartyCategory category);

    @EntityGraph(attributePaths = {"createdBy", "createdBy.image", "show", "show.facility"})
    Slice<Party> findSliceWithByCreatedByAndCategoryAndUseYnIsTrue(
            Pageable pageable, Member createdBy, PartyCategory category);

    long countByCreatedByAndUseYnIsTrue(Member createdBy);
}
