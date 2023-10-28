package org.cmc.curtaincall.domain.core;

import org.springframework.context.ApplicationEvent;

public abstract class AbstractDomainEvent<T> extends ApplicationEvent {

    protected AbstractDomainEvent(T id) {
        super(id);
    }

    @Override
    public T getSource() {
        return (T) super.getSource();
    }
}
