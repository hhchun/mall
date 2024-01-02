package com.hhchun.mall.user.support.decision;

import com.google.common.collect.Lists;
import com.hhchun.mall.user.support.provider.Permission;
import com.hhchun.mall.user.support.provider.SubjectOwnedPermissionsProvider;
import com.hhchun.mall.user.support.provider.TargetRequiredPermissionsProvider;

import java.util.List;
import java.util.Optional;


public class AffirmativeAccessDecision extends AbstractAccessDecision {

    public AffirmativeAccessDecision(final SubjectOwnedPermissionsProvider sop,
                                     final TargetRequiredPermissionsProvider trs) throws IllegalArgumentException {
        super(sop, trs);
    }

    @Override
    public boolean decide() {
        final List<Permission> subjectOwnedPermissions = Optional.ofNullable(sop.provide()).orElse(Lists.newArrayList());
        final List<Permission> targetAccessiblePermissions = Optional.ofNullable(trs.provide()).orElse(Lists.newArrayList());
        for (Permission targetAccessiblePermission : targetAccessiblePermissions) {
            if (subjectOwnedPermissions.contains(targetAccessiblePermission)) {
                return true;
            }
        }
        return false;
    }

}
