package org.cmc.curtaincall.web.party;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.core.OptimisticLock;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.party.*;
import org.cmc.curtaincall.domain.party.repository.PartyMemberRepository;
import org.cmc.curtaincall.domain.party.repository.PartyRepository;
import org.cmc.curtaincall.domain.party.response.PartyHelper;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.repository.ShowRepository;
import org.cmc.curtaincall.web.common.response.IdResult;
import org.cmc.curtaincall.web.party.request.PartyCreate;
import org.cmc.curtaincall.web.party.request.PartyEdit;
import org.cmc.curtaincall.web.party.response.PartyParticipatedResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartyService {

    private final PartyRepository partyRepository;

    private final ShowRepository showRepository;

    private final PartyMemberRepository partyMemberRepository;

    private final PartyMemberIdValidator memberIdValidator;

    @Transactional
    public IdResult<Long> create(PartyCreate partyCreate) {
        Party party = partyRepository.save(Party.builder()
                .show(Optional.ofNullable(partyCreate.getShowId())
                        .flatMap(showRepository::findById)
                        .filter(Show::getUseYn)
                        .orElse(null))
                .showAt(partyCreate.getShowAt())
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
        Party party = PartyHelper.get(partyId, partyRepository);
        party.participate(memberId, memberIdValidator);
    }

    public List<PartyParticipatedResponse> areParticipated(MemberId memberId, List<Long> partyIds) {
        List<Party> parties = partyIds.stream()
                .map(partyRepository::getReferenceById)
                .toList();
        Set<PartyId> participatedPartyIds = Stream.of(
                        partyMemberRepository.findAllByMemberIdAndPartyIn(memberId, parties).stream()
                                .map(PartyMember::getParty)
                                .map(Party::getId)
                                .map(PartyId::new),
                        partyRepository.findAllIdByCreatedByAndParty(new CreatorId(memberId), parties).stream()
                )
                .flatMap(Function.identity())
                .collect(Collectors.toSet());
        return partyIds.stream()
                .map(PartyId::new)
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

    private boolean isParticipated(MemberId memberId, Party party) {
        return party.getPartyMembers().stream()
                .anyMatch(partyMember -> Objects.equals(partyMember.getMemberId(), memberId))
                || Objects.equals(party.getCreatedBy().getMemberId(), memberId);
    }

    public boolean isOwnedByMember(PartyId partyId, MemberId memberId) {
        Party party = PartyHelper.get(partyId, partyRepository);
        return Objects.equals(party.getCreatedBy().getMemberId(), memberId);
    }

    @Transactional
    public void delete(PartyId partyId) {
        Party party = PartyHelper.get(partyId, partyRepository);
        party.getPartyMembers().clear();
        partyRepository.delete(party);
    }

}
