package com.hhchun.mall.user.shop.config;

import com.hhchun.mall.user.shop.authorize.ShopAuthorizeFilter;
import com.hhchun.mall.user.shop.provider.ShopSubjectOwnedPermissionsProvider;
import com.hhchun.mall.user.shop.provider.ShopTargetRequiredPermissionsProvider;
import com.hhchun.mall.user.shop.provider.cache.ShopAccessPermissionCache;
import com.hhchun.mall.access.support.provider.SubjectOwnedPermissionsProvider;
import com.hhchun.mall.access.support.provider.TargetRequiredPermissionsProvider;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
@MapperScan("com.hhchun.mall.user.shop.dao")
public class MallAccessShopConfiguration {

    @Bean
    public SubjectOwnedPermissionsProvider shopSubjectOwnedPermissionsProvider(ShopAccessPermissionCache cache) {
        return new ShopSubjectOwnedPermissionsProvider(cache);
    }

    @Bean
    public TargetRequiredPermissionsProvider shopTargetAccessiblePermissionsProvider(ShopAccessPermissionCache cache) {
        return new ShopTargetRequiredPermissionsProvider(cache);
    }

    @Bean
    public FilterRegistrationBean<Filter> shopAuthorizeFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        Filter shopAuthorizeFilter = new ShopAuthorizeFilter();
        registration.setFilter(shopAuthorizeFilter);
        registration.addUrlPatterns("/*");
        registration.setName(ShopAuthorizeFilter.class.getName() + "RegistrationBean");
        registration.setOrder(ShopAuthorizeFilter.FILTER_REGISTRATION_ORDER);
        return registration;
    }
}
