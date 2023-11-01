package org.cmc.curtaincall.web.party;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.core.OptimisticLock;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.party.*;
import org.cmc.curtaincall.domain.party.repository.PartyMemberRepository;
import org.cmc.curtaincall.domain.party.repository.PartyRepository;
import org.cmc.curtaincall.domain.party.response.PartyHelper;
import org.cmc.curtaincall.domain.party.validation.PartyMemberIdValidator;
import org.cmc.curtaincall.domain.party.validation.PartyShowIdValidator;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.web.common.response.IdResult;
import org.cmc.curtaincall.web.party.request.PartyCreate;
import org.cmc.curtaincall.web.party.request.PartyEdit;
import org.cmc.curtaincall.web.party.response.PartyParticipatedResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartyService {

    private final PartyRepository partyRepository;

    private final PartyMemberRepository partyMemberRepository;

    private final PartyMemberIdValidator memberIdValidator;

    private final PartyShowIdValidator showIdValidator;

    @Transactional
    public IdResult<Long> create(PartyCreate partyCreate) {
        ShowId showId = new ShowId(partyCreate.getShowId());
        showIdValidator.validate(showId);
        Party party = partyRepository.save(Party.builder()
                .showId(showId)
                .partyAt(partyCreate.getShowAt())
                .title(partyCreate.getTitle())
                .content(partyCreate.getContent())
                .maxMemberNum(partyCreate.getMaxMemberNum())
                .category(partyCreate.getCategory())
                .build()
        );

        return new IdResult<>(party.getId());
    }

    @Transactional
    @OptimisticLock
    public void participate(final PartyId partyId, final MemberId memberId) {
        memberIdValidator.validate(memberId);
        Party party = PartyHelper.get(partyId, partyRepository);
        party.participate(memberId);
    }

    public List<PartyParticipatedResponse> areParticipated(final MemberId memberId, final List<PartyId> partyIds) {
        List<Party> parties = partyIds.stream()
                .map(PartyId::getId)
                .map(partyRepository::getReferenceById)
                .toList();
        Set<PartyId> participatedPartyIds = Stream.of(
                        partyMemberRepository.findAllByMemberIdAndPartyIn(memberId, parties).stream()
                                .map(PartyMember::getParty)
                                .map(Party::getId)
                                .map(PartyId::new),
                        partyRepository.findAllIdByCreatedByAndPartyIn(new CreatorId(memberId), parties).stream()
                )
                .flatMap(Function.identity())
                .collect(Collectors.toSet());
        return partyIds.stream()
                .map(partyId -> new PartyParticipatedResponse(partyId.getId(), participatedPartyIds.contains(partyId)))
                .toList();
    }

    @Transactional
    public void edit(PartyId id, PartyEdit partyEdit) {
        Party party = PartyHelper.get(id, partyRepository);

        PartyEditor editor = party.toEditor()
                .title(partyEdit.getTitle())
                .content(partyEdit.getContent())
                .build();

        party.edit(editor);
    }

    @Transactional
    public void delete(PartyId partyId) {
        Party party = PartyHelper.get(partyId, partyRepository);
        party.getPartyMembers().clear();
        partyRepository.delete(party);
    }

}
