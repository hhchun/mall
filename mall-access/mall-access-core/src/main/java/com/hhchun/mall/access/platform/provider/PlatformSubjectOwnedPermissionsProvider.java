package com.hhchun.mall.access.platform.provider;

import com.google.common.collect.Lists;
import com.hhchun.mall.access.support.provider.Permission;
import com.hhchun.mall.access.support.provider.SubjectOwnedPermissionsProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class PlatformSubjectOwnedPermissionsProvider implements SubjectOwnedPermissionsProvider {

    @Override
    public List<Permission> provide() {
        return Lists.newArrayList(new Permission("develop"));
    }
}
