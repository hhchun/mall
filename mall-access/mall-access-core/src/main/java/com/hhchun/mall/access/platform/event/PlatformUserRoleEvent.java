package com.hhchun.mall.access.platform.event;

import org.springframework.context.ApplicationEvent;

public class PlatformUserRoleEvent extends ApplicationEvent {
    private final Action action;
    private final Long userId;

    public PlatformUserRoleEvent(Object source, Action action, Long userId) {
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
