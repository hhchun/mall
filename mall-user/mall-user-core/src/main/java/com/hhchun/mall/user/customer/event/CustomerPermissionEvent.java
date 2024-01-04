package com.hhchun.mall.user.customer.event;

import org.springframework.context.ApplicationEvent;

public class CustomerPermissionEvent extends ApplicationEvent {
    private final Action action;
    private final Long permissionId;

    public CustomerPermissionEvent(Object source, Action action, Long permissionId) {
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
