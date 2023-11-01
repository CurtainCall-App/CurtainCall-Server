package org.cmc.curtaincall.domain.party.repository;

import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.party.Party;
import org.cmc.curtaincall.domain.party.PartyId;

import java.util.Collection;
import java.util.List;

public interface PartyRepositoryCustom {

    List<PartyId> findAllIdByCreatedByAndPartyIn(CreatorId createdBy, Collection<Party> ids);
}
