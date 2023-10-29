package org.cmc.curtaincall.domain.common;

import com.querydsl.core.types.OrderSpecifier;

import java.util.Arrays;
import java.util.Objects;

public final class RepositoryHelper {

    public static OrderSpecifier<?>[] filterNullOrderByArr(OrderSpecifier<?>... o) {
        return Arrays.stream(o)
                .filter(Objects::nonNull)
                .toArray(OrderSpecifier[]::new);
    }

    private RepositoryHelper() {
        throw new UnsupportedOperationException();
    }
}
