package org.cmc.curtaincall.domain.review.convert;

import org.cmc.curtaincall.domain.review.ShowReviewId;
import org.springframework.core.convert.converter.Converter;

public class StringToShowReviewIdConverter implements Converter<String, ShowReviewId> {

    @Override
    public ShowReviewId convert(String source) {
        return new ShowReviewId(Long.parseLong(source));
    }
}
