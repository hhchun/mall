package com.hhchun.mall.access.support.decision;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.hhchun.mall.access.support.provider.SubjectOwnedPermissionsProvider;
import com.hhchun.mall.access.support.provider.Permission;
import com.hhchun.mall.access.support.provider.TargetRequiredPermissionsProvider;

import java.util.List;
import java.util.Optional;

public class AbstractAccessDecision implements AccessDecision {
    /**
     * 当前操作者拥有的权限提供者
     */
    protected final SubjectOwnedPermissionsProvider sop;
    /**
     * 受保护目标资源可访问(可到达)的权限提供者
     */
    protected final TargetRequiredPermissionsProvider trs;

    public AbstractAccessDecision(final SubjectOwnedPermissionsProvider sop,
                                  final TargetRequiredPermissionsProvider trs) throws IllegalArgumentException {
        Preconditions.checkArgument(sop != null, "sop == null!");
        Preconditions.checkArgument(trs != null, "trs == null!");
        this.sop = sop;
        this.trs = trs;
    }

    @Override
    public boolean decide() {
        final List<Permission> subjectOwnedPermissions = Optional.ofNullable(sop.provide()).orElse(Lists.newArrayList());
        final List<Permission> targetAccessiblePermissions = Optional.ofNullable(trs.provide()).orElse(Lists.newArrayList());
        for (Permission targetAccessiblePermission : targetAccessiblePermissions) {
            if (!subjectOwnedPermissions.contains(targetAccessiblePermission)) {
                return false;
            }
        }
        return true;
    }
}
