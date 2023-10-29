package org.cmc.curtaincall.domain.core;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity extends BaseTimeEntity {

    @CreatedBy
    @JoinColumn(name = "created_by", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private CreatorId createdBy;
}
