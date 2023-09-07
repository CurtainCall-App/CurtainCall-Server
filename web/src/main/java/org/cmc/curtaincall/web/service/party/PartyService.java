package org.cmc.curtaincall.web.service.party;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.OptimisticLock;
import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.member.repository.MemberRepository;
import org.cmc.curtaincall.domain.party.Party;
import org.cmc.curtaincall.domain.party.PartyCategory;
import org.cmc.curtaincall.domain.party.PartyEditor;
import org.cmc.curtaincall.domain.party.PartyMember;
import org.cmc.curtaincall.domain.party.repository.PartyMemberRepository;
import org.cmc.curtaincall.domain.party.repository.PartyQueryRepository;
import org.cmc.curtaincall.domain.party.repository.PartyRepository;
import org.cmc.curtaincall.domain.party.request.PartySearchParam;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.repository.ShowRepository;
import org.cmc.curtaincall.web.exception.AlreadyClosedPartyException;
import org.cmc.curtaincall.web.exception.EntityNotFoundException;
import org.cmc.curtaincall.web.service.common.response.IdResult;
import org.cmc.curtaincall.web.service.party.request.PartyCreate;
import org.cmc.curtaincall.web.service.party.request.PartyEdit;
import org.cmc.curtaincall.web.service.party.response.PartyDetailResponse;
import org.cmc.curtaincall.web.service.party.response.PartyParticipatedResponse;
import org.cmc.curtaincall.web.service.party.response.PartyResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartyService {

    private final PartyRepository partyRepository;

    private final ShowRepository showRepository;

    private final MemberRepository memberRepository;

    private final PartyQueryRepository partyQueryRepository;

    private final PartyMemberRepository partyMemberRepository;

    public PartyDetailResponse getDetail(Long id) {
        Party party = getPartyById(id);
        return PartyDetailResponse.of(party);
    }

    public Slice<PartyResponse> getList(Pageable pageable, PartyCategory category) {
        return partyRepository.findSliceWithByCategoryAndUseYnIsTrue(pageable, category)
                .map(PartyResponse::of);
    }

    public Slice<PartyResponse> search(Pageable pageable, PartySearchParam searchParam) {
        return partyQueryRepository.search(pageable, searchParam)
                .map(PartyResponse::of);
    }

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
    public void participate(Long partyId, Long memberId) {
        Party party = getPartyById(partyId);
        boolean laterParty = Optional.ofNullable(party.getShowAt())
                .filter(showAt -> LocalDateTime.now().isAfter(showAt))
                .isPresent();
        if (Boolean.TRUE.equals(party.getClosed()) || laterParty) {
            throw new AlreadyClosedPartyException("Party id=" + partyId);
        }

        if (isParticipated(memberId, party)) {
            return;
        }

        Member member = getMemberById(memberId);
        party.participate(member);
    }

    public List<PartyParticipatedResponse> areParticipated(Long memberId, List<Long> partyIds) {
        Member member = memberRepository.getReferenceById(memberId);
        List<Party> parties = partyIds.stream()
                .map(partyRepository::getReferenceById)
                .toList();
        Set<Long> participatedPartyIds = partyMemberRepository.findAllByMemberAndPartyIn(member, parties).stream()
                .map(PartyMember::getParty)
                .map(Party::getId)
                .collect(Collectors.toSet());
        return partyIds.stream()
                .map(partyId -> new PartyParticipatedResponse(partyId, participatedPartyIds.contains(partyId)))
                .toList();
    }

    @Transactional
    public void edit(Long id, PartyEdit partyEdit) {
        Party party = getPartyById(id);

        PartyEditor editor = party.toEditor()
                .title(partyEdit.getTitle())
                .content(partyEdit.getContent())
                .build();

        party.edit(editor);
    }

    private boolean isParticipated(Long memberId, Party party) {
        return party.getPartyMembers().stream()
                .anyMatch(partyMember -> Objects.equals(partyMember.getMember().getId(), memberId))
                || Objects.equals(party.getCreatedBy().getId(), memberId);
    }

    public boolean isOwnedByMember(Long partyId, Long memberId) {
        Party party = getPartyById(partyId);
        return Objects.equals(party.getCreatedBy().getId(), memberId);
    }

    @Transactional
    public void delete(Long partyId) {
        Party party = getPartyById(partyId);
        party.getPartyMembers().clear();
        partyRepository.delete(party);
    }

    private Party getPartyById(Long id) {
        return partyRepository.findById(id)
                .filter(Party::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Party id=" + id));
    }

    private Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .filter(Member::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Member id=" + id));
    }

}
