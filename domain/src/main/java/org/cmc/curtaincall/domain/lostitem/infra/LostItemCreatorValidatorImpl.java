package org.cmc.curtaincall.domain.lostitem.infra;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.lostitem.LostItemId;
import org.cmc.curtaincall.domain.lostitem.dao.LostItemExistsDao;
import org.cmc.curtaincall.domain.lostitem.exception.LostItemAccessDeniedException;
import org.cmc.curtaincall.domain.lostitem.validation.LostItemCreatorValidator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LostItemCreatorValidatorImpl implements LostItemCreatorValidator {

    private final LostItemExistsDao lostItemExistsDao;

    @Override
    public void validate(final LostItemId lostItemId, final CreatorId creatorId) {
        if (!lostItemExistsDao.existsByIdAndCreatedBy(lostItemId, creatorId)) {
            throw new LostItemAccessDeniedException(lostItemId, creatorId.getMemberId());
        }
    }
}
