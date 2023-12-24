package com.hhchun.mall.access.platform.config;

import com.hhchun.mall.access.platform.authorize.PlatformAuthorizeFilter;
import com.hhchun.mall.access.platform.provider.PlatformSubjectOwnedPermissionsProvider;
import com.hhchun.mall.access.platform.provider.PlatformTargetRequiredPermissionsProvider;
import com.hhchun.mall.access.platform.provider.cache.PlatformAccessPermissionCache;
import com.hhchun.mall.access.support.provider.SubjectOwnedPermissionsProvider;
import com.hhchun.mall.access.support.provider.TargetRequiredPermissionsProvider;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
@MapperScan("com.hhchun.mall.access.platform.dao")
public class MallAccessPlatformConfiguration {

    @Bean
    public SubjectOwnedPermissionsProvider platformSubjectOwnedPermissionsProvider(PlatformAccessPermissionCache cache) {
        return new PlatformSubjectOwnedPermissionsProvider(cache);
    }

    @Bean
    public TargetRequiredPermissionsProvider platformTargetAccessiblePermissionsProvider(PlatformAccessPermissionCache cache) {
        return new PlatformTargetRequiredPermissionsProvider(cache);
    }

    @Bean
    public FilterRegistrationBean<Filter> daemonAuthorizeFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        Filter platformAuthorizeFilter = new PlatformAuthorizeFilter();
        registration.setFilter(platformAuthorizeFilter);
        registration.addUrlPatterns("/*");
        registration.setName(PlatformAuthorizeFilter.class.getName() + "RegistrationBean");
        registration.setOrder(PlatformAuthorizeFilter.FILTER_REGISTRATION_ORDER);
        return registration;
    }
}
