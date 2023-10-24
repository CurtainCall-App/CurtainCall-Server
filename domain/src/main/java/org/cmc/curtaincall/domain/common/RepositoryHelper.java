package org.cmc.curtaincall.domain.common;

import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class RepositoryHelper {

    public static <T> Slice<T> createSlice(List<T> content, Pageable pageable) {
        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    public static OrderSpecifier<?>[] filterNullOrderByArr(OrderSpecifier<?>... o) {
        return Arrays.stream(o)
                .filter(Objects::nonNull)
                .toArray(OrderSpecifier[]::new);
    }

    private RepositoryHelper() {
        throw new UnsupportedOperationException();
    }
}
