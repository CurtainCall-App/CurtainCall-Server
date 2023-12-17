package org.cmc.curtaincall.domain.core;

public @interface SortParam {

    OrderParam[] value() default {};
}
