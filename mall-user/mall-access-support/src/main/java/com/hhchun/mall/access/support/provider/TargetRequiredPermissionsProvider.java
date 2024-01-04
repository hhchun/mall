package com.hhchun.mall.access.support.provider;

import java.util.List;

/**
 * 目标需要的权限
 */
public interface TargetRequiredPermissionsProvider {

    boolean support();

    List<Permission> provide();
}
