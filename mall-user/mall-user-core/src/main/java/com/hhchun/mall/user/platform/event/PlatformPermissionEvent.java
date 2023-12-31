package com.hhchun.mall.user.platform.event;

import org.springframework.context.ApplicationEvent;

public class PlatformPermissionEvent extends ApplicationEvent {
    private final Action action;
    private final Long permissionId;

    public PlatformPermissionEvent(Object source, Action action, Long permissionId) {
        super(source);
        this.action = action;
        this.permissionId = permissionId;
    }

    public Action getAction() {
        return action;
    }

    public Long getPermissionId() {
        return permissionId;
    }
}
