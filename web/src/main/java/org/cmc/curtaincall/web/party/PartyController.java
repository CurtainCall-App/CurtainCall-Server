package org.cmc.curtaincall.web.party;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.party.PartyId;
import org.cmc.curtaincall.domain.party.validation.PartyCreatorValidator;
import org.cmc.curtaincall.web.common.response.IdResult;
import org.cmc.curtaincall.web.party.request.PartyCreate;
import org.cmc.curtaincall.web.party.request.PartyEdit;
import org.cmc.curtaincall.web.security.LoginMemberId;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PartyController {

    private final PartyService partyService;

    private final PartyCreatorValidator partyCreatorValidator;

    @PostMapping("/parties")
    public IdResult<Long> createParty(@RequestBody @Validated PartyCreate partyCreate) {
        return partyService.create(partyCreate);
    }

    @InitBinder("partyCreate")
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(new PartyCreateValidator());
    }

    @DeleteMapping("/parties/{partyId}")
    public void deleteParty(@PathVariable Long partyId, @LoginMemberId MemberId memberId) {
        partyCreatorValidator.validate(new PartyId(partyId), new CreatorId(memberId));
        partyService.delete(new PartyId(partyId));
    }

    @PatchMapping("/parties/{partyId}")
    public void editParty(
            @PathVariable Long partyId, @RequestBody @Validated PartyEdit partyEdit,
            @LoginMemberId MemberId memberId) {
        partyCreatorValidator.validate(new PartyId(partyId), new CreatorId(memberId));
        partyService.edit(new PartyId(partyId), partyEdit);
    }

    @PutMapping("/member/parties/{partyId}")
    public void participateParty(@PathVariable Long partyId, @LoginMemberId MemberId memberId) {
        partyService.participate(new PartyId(partyId), memberId);
    }

}
