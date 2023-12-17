package org.cmc.curtaincall.domain.core;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

// TODO 메시지 생성 방식 개선...
// TODO ConstraintViolationException 말고 MethodArgumentInvalidException 사용할 수 있는지...
public class PageableAllowedSortValidator implements ConstraintValidator<AllowedSort, Pageable> {

    private List<Sort> allowedSorts;

    @Override
    public void initialize(final AllowedSort allowedSort) {
        this.allowedSorts = Arrays.stream(allowedSort.value())
                .map(sortParam -> Arrays.stream(sortParam.value())
                        .map(orderParam -> new Sort.Order(orderParam.direction(), orderParam.property()))
                        .toArray(Sort.Order[]::new)
                )
                .map(Sort::by)
                .toList();
    }

    @Override
    public boolean isValid(final Pageable pageable, final ConstraintValidatorContext context) {
        return allowedSorts.contains(pageable.getSort());
    }
}
