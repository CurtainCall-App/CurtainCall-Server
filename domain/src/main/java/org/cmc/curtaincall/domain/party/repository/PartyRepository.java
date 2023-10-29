package org.cmc.curtaincall.domain.party.repository;

import jakarta.persistence.LockModeType;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.party.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface PartyRepository extends JpaRepository<Party, Long>, PartyRepositoryCustom {

    long countByCreatedByAndUseYnIsTrue(CreatorId createdBy);

    @Lock(LockModeType.OPTIMISTIC)
    Optional<Party> findWithLockById(Long id);

}
