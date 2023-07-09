package org.cmc.curtaincall.domain.core;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class BaseUseYnEntity {

    @Column(name = "use_yn", nullable = false)
    private Boolean useYn = true;

    public void delete() {
        useYn = false;
    }
}
