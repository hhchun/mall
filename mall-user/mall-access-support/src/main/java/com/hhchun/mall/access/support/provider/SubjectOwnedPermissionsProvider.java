package com.hhchun.mall.access.support.provider;

import java.util.List;

/**
 * 访问者拥有的权限
 */
public interface SubjectOwnedPermissionsProvider {

    boolean support();

    List<Permission> provide();
}
