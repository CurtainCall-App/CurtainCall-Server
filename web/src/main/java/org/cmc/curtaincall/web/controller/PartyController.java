package org.cmc.curtaincall.web.controller;

import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.party.PartyCategory;
import org.cmc.curtaincall.domain.party.request.PartySearchParam;
import org.cmc.curtaincall.web.exception.EntityAccessDeniedException;
import org.cmc.curtaincall.web.security.annotation.LoginMemberId;
import org.cmc.curtaincall.web.common.response.IdResult;
import org.cmc.curtaincall.web.service.party.PartyService;
import org.cmc.curtaincall.web.service.party.request.PartyCreate;
import org.cmc.curtaincall.web.service.party.request.PartyEdit;
import org.cmc.curtaincall.web.service.party.response.PartyDetailResponse;
import org.cmc.curtaincall.web.service.party.response.PartyParticipatedResponse;
import org.cmc.curtaincall.web.service.party.response.PartyResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PartyController {

    private final PartyService partyService;

    @GetMapping("/parties")
    public Slice<PartyResponse> getPartyList(
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam PartyCategory category) {
        return partyService.getList(pageable, category);
    }

    @GetMapping("/search/party")
    public Slice<PartyResponse> searchParty(
            Pageable pageable, @ModelAttribute @Validated PartySearchParam partySearchParam) {
        return partyService.search(pageable, partySearchParam);
    }

    @GetMapping("/parties/{partyId}")
    public PartyDetailResponse getPartyDetail(@PathVariable Long partyId) {
        return partyService.getDetail(partyId);
    }

    @PostMapping("/parties")
    public IdResult<Long> createParty(@RequestBody @Validated PartyCreate partyCreate) {
        return partyService.create(partyCreate);
    }

    @DeleteMapping("/parties/{partyId}")
    public void deleteParty(@PathVariable Long partyId, @LoginMemberId MemberId memberId) {
        if (!partyService.isOwnedByMember(partyId, memberId.getId())) {
            throw new EntityAccessDeniedException("partyId=" + partyId + "memberId=" + memberId);
        }
        partyService.delete(partyId);
    }

    @PatchMapping("/parties/{partyId}")
    public void editParty(
            @PathVariable Long partyId, @RequestBody @Validated PartyEdit partyEdit,
            @LoginMemberId MemberId memberId) {
        if (!partyService.isOwnedByMember(partyId, memberId.getId())) {
            throw new EntityAccessDeniedException("partyId=" + partyId + "memberId=" + memberId);
        }
        partyService.edit(partyId, partyEdit);
    }

    @PutMapping("/member/parties/{partyId}")
    public void participateParty(@PathVariable Long partyId, @LoginMemberId MemberId memberId) {
        partyService.participate(partyId, memberId.getId());
    }

    @GetMapping("/member/participated")
    public Slice<PartyParticipatedResponse> getParticipated(
            @RequestParam @Validated @Size(max = 100) List<Long> partyIds, @LoginMemberId MemberId memberId
    ) {
        List<PartyParticipatedResponse> partyParticipatedResponses = partyService.areParticipated(memberId.getId(), partyIds);
        return new SliceImpl<>(partyParticipatedResponses);
    }
}
