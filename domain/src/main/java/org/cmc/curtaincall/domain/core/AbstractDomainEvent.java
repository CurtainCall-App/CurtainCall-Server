package org.cmc.curtaincall.domain.core;

import org.springframework.context.ApplicationEvent;

public abstract class AbstractDomainEvent<T> extends ApplicationEvent {

    protected AbstractDomainEvent(T source) {
        super(source);
    }

    @Override
    public T getSource() {
        return (T) super.getSource();
    }
}
