package org.cmc.curtaincall.web.party;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.party.PartyId;
import org.cmc.curtaincall.domain.party.validation.PartyCreatorValidator;
import org.cmc.curtaincall.web.common.response.IdResult;
import org.cmc.curtaincall.web.party.request.PartyCreate;
import org.cmc.curtaincall.web.party.request.PartyEdit;
import org.cmc.curtaincall.web.security.config.LoginMemberId;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PartyController {

    private final PartyService partyService;

    private final PartyCreatorValidator partyCreatorValidator;

    private final PartyDeleteService partyDeleteService;

    private final PartyParticipationService partyParticipationService;

    @PostMapping("/parties")
    public IdResult<PartyId> createParty(
            @RequestBody @Validated PartyCreate partyCreate, @LoginMemberId MemberId memberId
    ) {
        return new IdResult<>(partyService.create(partyCreate, new CreatorId(memberId)));
    }

    @DeleteMapping("/parties/{partyId}")
    public void deleteParty(@PathVariable PartyId partyId, @LoginMemberId MemberId memberId) {
        partyCreatorValidator.validate(partyId, new CreatorId(memberId));
        partyDeleteService.delete(partyId);
    }

    @PatchMapping("/parties/{partyId}")
    public void editParty(
            @PathVariable PartyId partyId, @RequestBody @Validated PartyEdit partyEdit,
            @LoginMemberId MemberId memberId) {
        partyCreatorValidator.validate(partyId, new CreatorId(memberId));
        partyService.edit(partyId, partyEdit);
    }

    @PutMapping("/member/parties/{partyId}")
    public void participateParty(@PathVariable PartyId partyId, @LoginMemberId MemberId memberId) {
        partyParticipationService.participate(partyId, memberId);
    }

    @DeleteMapping("/member/parties/{partyId}")
    public void cancelParticipation(
            @PathVariable final PartyId partyId, @LoginMemberId final MemberId memberId) {
        partyParticipationService.leave(partyId, memberId);
    }

}
