package com.hhchun.mall.access.platform.event;

import org.springframework.context.ApplicationEvent;

public class PlatformRoleEvent extends ApplicationEvent {
    private final Action action;
    private final Long roleId;

    public PlatformRoleEvent(Object source, Action action, Long roleId) {
        super(source);
        this.action = action;
        this.roleId = roleId;
    }

    public Action getAction() {
        return action;
    }

    public Long getRoleId() {
        return roleId;
    }
}
