package com.hhchun.mall.access.platform.event;

import com.google.common.collect.Lists;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class PlatformMenuPermissionEvent extends ApplicationEvent {
    private final Action action;
    private final Long menuId;
    private final String lowerRoute;
    private final List<Long> lowerMenuIds;

    public PlatformMenuPermissionEvent(Object source, Action action, Long menuId, String lowerRoute) {
        this(source, action, menuId, lowerRoute, Lists.newArrayList());
    }

    public PlatformMenuPermissionEvent(Object source, Action action, Long menuId, String lowerRoute, List<Long> lowerMenuIds) {
        super(source);
        this.action = action;
        this.menuId = menuId;
        this.lowerRoute = lowerRoute;
        this.lowerMenuIds = lowerMenuIds;
    }

    public Action getAction() {
        return action;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getLowerRoute() {
        return lowerRoute;
    }

    public List<Long> getLowerMenuIds() {
        return lowerMenuIds;
    }
}
