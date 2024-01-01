package com.hhchun.mall.access.support.provider;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DelegatingTargetAccessiblePermissionsProvider implements TargetRequiredPermissionsProvider {
    public static final String BEAN_NAME = "com.hhchun.mall.access.support.provider.DelegatingTargetAccessiblePermissionsProvider";

    private final Set<TargetRequiredPermissionsProvider> trs;

    public DelegatingTargetAccessiblePermissionsProvider(Set<TargetRequiredPermissionsProvider> trs) {
        Preconditions.checkArgument(!CollectionUtils.isEmpty(trs), "trs is empty!");
        this.trs = trs;
    }

    @Override
    public List<Permission> provide() {
        return trs.stream().flatMap(trs -> Optional.ofNullable(trs.provide()).orElse(Lists.newArrayList())
                .stream()).collect(Collectors.toList());
    }
}
