package org.cmc.curtaincall.domain.core;

import org.springframework.context.ApplicationEvent;

public abstract class AbstractDomainEvent<ID> extends ApplicationEvent {

    protected AbstractDomainEvent(ID id) {
        super(id);
    }

    public ID getId() {
        return (ID) getSource();
    }
}
