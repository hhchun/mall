package com.hhchun.mall.access.platform.event;

import org.springframework.context.ApplicationEvent;

import java.util.Collections;
import java.util.Set;

public class PlatformUserRoleEvent extends ApplicationEvent {
    private final Action action;
    private final Long userId;
    private final Set<Long> roleIds;

    public PlatformUserRoleEvent(Object source, Action action, Long userId, Set<Long> roleIds) {
        super(source);
        this.action = action;
        this.userId = userId;
        this.roleIds = roleIds;
    }

    public PlatformUserRoleEvent(Object source, Action action, Long userId) {
        this(source, action, userId, Collections.emptySet());
    }

    public Action getAction() {
        return action;
    }

    public Long getUserId() {
        return userId;
    }

    public Set<Long> getRoleIds() {
        return roleIds;
    }
}
