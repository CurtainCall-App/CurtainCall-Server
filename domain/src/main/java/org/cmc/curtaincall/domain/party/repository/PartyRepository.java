package org.cmc.curtaincall.domain.party.repository;

import org.cmc.curtaincall.domain.party.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long> {
}
