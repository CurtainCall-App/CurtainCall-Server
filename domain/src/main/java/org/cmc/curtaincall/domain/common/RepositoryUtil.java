package org.cmc.curtaincall.domain.common;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

public final class RepositoryUtil {

    public static <T> Slice<T> createSlice(List<T> content, Pageable pageable) {
        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    private RepositoryUtil() {
        throw new UnsupportedOperationException();
    }
}
