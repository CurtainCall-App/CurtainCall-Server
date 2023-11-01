package org.cmc.curtaincall.web.party;

import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.party.PartyCategory;
import org.cmc.curtaincall.domain.party.PartyId;
import org.cmc.curtaincall.domain.party.dao.PartyDao;
import org.cmc.curtaincall.domain.party.request.PartySearchParam;
import org.cmc.curtaincall.domain.party.response.*;
import org.cmc.curtaincall.web.common.response.ListResult;
import org.cmc.curtaincall.web.security.LoginMemberId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PartyQueryController {

    private final PartyDao partyDao;

    @GetMapping("/parties")
    public ListResult<PartyResponse> getPartyList(
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam PartyCategory category) {
        return new ListResult<>(partyDao.getList(pageable, category));
    }

    @GetMapping("/search/party")
    public ListResult<PartyResponse> searchParty(
            Pageable pageable, @ModelAttribute @Validated PartySearchParam partySearchParam) {
        return new ListResult<>(partyDao.search(pageable, partySearchParam));
    }

    @GetMapping("/parties/{partyId}")
    public PartyDetailResponse getPartyDetail(@PathVariable Long partyId) {
        return partyDao.getDetail(new PartyId(partyId));
    }

    @GetMapping("/members/{memberId}/recruitments")
    public ListResult<PartyRecruitmentResponse> getRecruitmentList(
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) PartyCategory category, @PathVariable Long memberId
    ) {
        return new ListResult<>(partyDao.getRecruitmentList(pageable, new MemberId(memberId), category));
    }

    @GetMapping("/members/{memberId}/participations")
    public ListResult<PartyParticipationResponse> getParticipationList(
            Pageable pageable,
            @RequestParam(required = false) PartyCategory category, @PathVariable Long memberId
    ) {
        return new ListResult<>(partyDao.getParticipationList(pageable, new MemberId(memberId), category));
    }

    @GetMapping("/member/participated")
    public ListResult<PartyParticipatedResponse> getParticipated(
            @RequestParam @Validated @Size(max = 100) List<Long> partyIds, @LoginMemberId MemberId memberId
    ) {
        List<PartyParticipatedResponse> partyParticipatedResponses = partyDao.areParticipated(
                partyIds.stream().map(PartyId::new).toList(), memberId);
        return new ListResult<>(partyParticipatedResponses);
    }
}
