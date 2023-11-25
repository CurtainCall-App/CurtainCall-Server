package org.cmc.curtaincall.web.party;

import org.cmc.curtaincall.domain.party.PartyCategory;
import org.cmc.curtaincall.web.party.request.PartyCreate;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class PartyCreateValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return PartyCreate.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PartyCreate partyCreate = (PartyCreate) target;
        if (partyCreate.getCategory() == PartyCategory.ETC) {
            return;
        }

        if (partyCreate.getShowId() == null) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "showId", "required",
                    "기타 파티가 아닌 경우 공연 아이디가 필요합니다.");
        }
        if (partyCreate.getShowAt() == null) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "showAt", "required",
                    "기타 파티가 아닌 경우 공연 일시가 필요합니다.");
        }
    }
}
