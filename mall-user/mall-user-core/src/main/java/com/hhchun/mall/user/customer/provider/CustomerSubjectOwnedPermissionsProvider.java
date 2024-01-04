package com.hhchun.mall.user.customer.provider;

import com.hhchun.mall.access.support.provider.Permission;
import com.hhchun.mall.access.support.provider.SubjectOwnedPermissionsProvider;
import com.hhchun.mall.user.customer.authorize.CustomerUserSubjectHolder;
import com.hhchun.mall.user.customer.provider.cache.CustomerAccessPermissionCache;
import com.hhchun.mall.user.fingerprint.Fingerprint;
import com.hhchun.mall.user.fingerprint.FingerprintHolder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class CustomerSubjectOwnedPermissionsProvider implements SubjectOwnedPermissionsProvider {

    private final CustomerAccessPermissionCache cache;

    public CustomerSubjectOwnedPermissionsProvider(CustomerAccessPermissionCache cache) {
        this.cache = cache;
    }

    @Override
    public boolean support() {
        Fingerprint fingerprint = FingerprintHolder.getFingerprint();
        if (fingerprint != null) {
            return Fingerprint.CLIENT_TYPE_CUSTOMER.equals(fingerprint.getClientType());
        }
        return true;
    }

    @Override
    public List<Permission> provide() {
        Long customerUserId = CustomerUserSubjectHolder.getCustomerUserId();
        List<Permission> overt = cache.getOvertPermission();
        List<Permission> owned = cache.getOwnedPermission(customerUserId);
        return Stream.concat(overt.stream(), owned.stream()).collect(Collectors.toList());
    }
}
