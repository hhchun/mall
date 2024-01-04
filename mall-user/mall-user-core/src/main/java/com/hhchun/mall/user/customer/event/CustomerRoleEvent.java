package com.hhchun.mall.user.customer.event;

import org.springframework.context.ApplicationEvent;

public class CustomerRoleEvent extends ApplicationEvent {
    private final Action action;
    private final Long roleId;

    public CustomerRoleEvent(Object source, Action action, Long roleId) {
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
