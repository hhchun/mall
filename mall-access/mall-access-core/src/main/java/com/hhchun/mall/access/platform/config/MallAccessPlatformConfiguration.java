package com.hhchun.mall.access.platform.config;

import com.hhchun.mall.access.platform.authorize.PlatformAuthorizeFilter;
import com.hhchun.mall.access.platform.provider.PlatformSubjectOwnedPermissionsProvider;
import com.hhchun.mall.access.platform.provider.PlatformTargetRequiredPermissionsProvider;
import com.hhchun.mall.access.support.provider.SubjectOwnedPermissionsProvider;
import com.hhchun.mall.access.support.provider.TargetRequiredPermissionsProvider;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class MallAccessPlatformConfiguration {
    @Bean
    public SubjectOwnedPermissionsProvider platformSubjectOwnedPermissionsProvider() {
        return new PlatformSubjectOwnedPermissionsProvider();
    }

    @Bean
    public TargetRequiredPermissionsProvider platformTargetAccessiblePermissionsProvider() {
        return new PlatformTargetRequiredPermissionsProvider();
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
