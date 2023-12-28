package com.hhchun.mall.access.platform.event;

import org.springframework.context.ApplicationEvent;

import java.util.Collections;
import java.util.Set;

public class PlatformRolePermissionEvent extends ApplicationEvent {
    private final Action action;
    private final Long roleId;
    private final Set<Long> permissionIds;

    public PlatformRolePermissionEvent(Object source, Action action, Long roleId, Set<Long> permissionIds) {
        super(source);
        this.action = action;
        this.roleId = roleId;
        this.permissionIds = permissionIds;
    }

    public PlatformRolePermissionEvent(Object source, Action action, Long roleId) {
        this(source, action, roleId, Collections.emptySet());
    }

    public Action getAction() {
        return action;
    }

    public Long getRoleId() {
        return roleId;
    }

    public Set<Long> getPermissionIds() {
        return permissionIds;
    }
}
