package org.cmc.curtaincall.batch.job.common;

public record WithPresent<T>(
        T value,
        boolean present
) {
}
