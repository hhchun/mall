package com.hhchun.mall.user.support.provider;

import java.util.List;

/**
 * 访问者拥有的权限
 */
public interface SubjectOwnedPermissionsProvider {
    List<Permission> provide();
}
