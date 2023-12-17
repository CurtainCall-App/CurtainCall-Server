package org.cmc.curtaincall.domain.core;

import org.springframework.data.domain.Sort;

public @interface OrderParam {

    String property();

    Sort.Direction direction() default Sort.Direction.ASC;
}
