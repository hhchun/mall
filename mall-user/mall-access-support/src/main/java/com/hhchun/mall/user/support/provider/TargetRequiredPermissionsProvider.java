package com.hhchun.mall.user.support.provider;

import java.util.List;

/**
 * 目标需要的权限
 */
public interface TargetRequiredPermissionsProvider {
    List<Permission> provide();
}
