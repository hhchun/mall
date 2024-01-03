package com.hhchun.mall.user.shop.event;

import org.springframework.context.ApplicationEvent;

public class ShopPermissionEvent extends ApplicationEvent {
    private final Action action;
    private final Long permissionId;

    public ShopPermissionEvent(Object source, Action action, Long permissionId) {
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
