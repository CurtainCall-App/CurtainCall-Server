package org.cmc.curtaincall.domain.review.infra;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.review.ShowReviewId;
import org.cmc.curtaincall.domain.review.dao.ShowReviewExistsDao;
import org.cmc.curtaincall.domain.review.exception.ShowReviewAccessDeniedException;
import org.cmc.curtaincall.domain.review.validation.ShowReviewCreatorValidator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShowReviewCreatorValidatorImpl implements ShowReviewCreatorValidator {

    private final ShowReviewExistsDao showReviewExistsDao;

    @Override
    public void validate(final ShowReviewId id, final CreatorId creatorId) {
        if (!showReviewExistsDao.existsByIdAndCreatedBy(id, creatorId)) {
            throw new ShowReviewAccessDeniedException(id, creatorId.getMemberId(),
                    "작성자에게만 허용된 기능입니다.");
        }
    }
}
