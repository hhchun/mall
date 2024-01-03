package com.hhchun.mall.user.shop.provider;

import com.hhchun.mall.user.shop.authorize.ShopUserSubjectHolder;
import com.hhchun.mall.user.shop.provider.cache.ShopAccessPermissionCache;
import com.hhchun.mall.user.support.provider.Permission;
import com.hhchun.mall.user.support.provider.SubjectOwnedPermissionsProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class ShopSubjectOwnedPermissionsProvider implements SubjectOwnedPermissionsProvider {

    private final ShopAccessPermissionCache cache;

    public ShopSubjectOwnedPermissionsProvider(ShopAccessPermissionCache cache) {
        this.cache = cache;
    }

    @Override
    public List<Permission> provide() {
        Long shopUserId = ShopUserSubjectHolder.getShopUserId();
        List<Permission> overt = cache.getOvertPermission();
        List<Permission> owned = cache.getOwnedPermission(shopUserId);
        return Stream.concat(overt.stream(), owned.stream()).collect(Collectors.toList());
    }
}
