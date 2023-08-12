package org.cmc.curtaincall.web.service.party;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.member.repository.MemberRepository;
import org.cmc.curtaincall.domain.party.Party;
import org.cmc.curtaincall.domain.party.repository.PartyRepository;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.repository.ShowRepository;
import org.cmc.curtaincall.web.exception.AlreadyClosedPartyException;
import org.cmc.curtaincall.web.exception.EntityNotFoundException;
import org.cmc.curtaincall.web.service.common.response.IdResult;
import org.cmc.curtaincall.web.service.party.request.PartyCreate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PartyService {

    private final PartyRepository partyRepository;

    private final ShowRepository showRepository;

    private final MemberRepository memberRepository;

    public IdResult<Long> create(PartyCreate partyCreate) {
        Show show = getShowById(partyCreate.getShowId());
        Party party = partyRepository.save(Party.builder()
                .show(show)
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
    public void participate(Long partyId, Long memberId) {
        Party party = getPartyById(partyId);
        if (Boolean.FALSE.equals(party.getClosed())) {
            throw new AlreadyClosedPartyException("Party id=" + partyId);
        }

        if (isParticipated(memberId, party)) {
            return;
        }

        Member member = getMemberById(memberId);
        party.participate(member);
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
        party.delete();
    }

    private Show getShowById(String id) {
        return showRepository.findById(id)
                .filter(Show::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Show id=" + id));
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
