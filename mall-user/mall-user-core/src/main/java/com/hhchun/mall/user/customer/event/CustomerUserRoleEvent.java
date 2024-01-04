package com.hhchun.mall.user.customer.event;

import org.springframework.context.ApplicationEvent;

public class CustomerUserRoleEvent extends ApplicationEvent {
    private final Action action;
    private final Long userId;

    public CustomerUserRoleEvent(Object source, Action action, Long userId) {
        super(source);
        this.action = action;
        this.userId = userId;
    }

    public Action getAction() {
        return action;
    }

    public Long getUserId() {
        return userId;
    }
}
