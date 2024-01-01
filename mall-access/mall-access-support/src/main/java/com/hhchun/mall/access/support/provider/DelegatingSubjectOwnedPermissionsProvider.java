package com.hhchun.mall.access.support.provider;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DelegatingSubjectOwnedPermissionsProvider implements SubjectOwnedPermissionsProvider {
    public static final String BEAN_NAME = "com.hhchun.mall.access.support.provider.DelegatingSubjectOwnedPermissionsProvider";

    private final Set<SubjectOwnedPermissionsProvider> sops;

    public DelegatingSubjectOwnedPermissionsProvider(Set<SubjectOwnedPermissionsProvider> sops) {
        Preconditions.checkArgument(!CollectionUtils.isEmpty(sops), "sops is empty!");
        this.sops = sops;
    }

    @Override
    public List<Permission> provide() {
        return sops.stream().flatMap(sop -> Optional.ofNullable(sop.provide()).orElse(Lists.newArrayList())
                .stream()).collect(Collectors.toList());
    }
}
