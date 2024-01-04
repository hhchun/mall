package com.hhchun.mall.user.customer.config;

import com.hhchun.mall.access.support.provider.SubjectOwnedPermissionsProvider;
import com.hhchun.mall.access.support.provider.TargetRequiredPermissionsProvider;
import com.hhchun.mall.user.customer.authorize.CustomerAuthorizeFilter;
import com.hhchun.mall.user.customer.provider.CustomerSubjectOwnedPermissionsProvider;
import com.hhchun.mall.user.customer.provider.CustomerTargetRequiredPermissionsProvider;
import com.hhchun.mall.user.customer.provider.cache.CustomerAccessPermissionCache;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
@MapperScan("com.hhchun.mall.user.customer.dao")
public class MallAccessCustomerConfiguration {

    @Bean
    public SubjectOwnedPermissionsProvider customerSubjectOwnedPermissionsProvider(CustomerAccessPermissionCache cache) {
        return new CustomerSubjectOwnedPermissionsProvider(cache);
    }

    @Bean
    public TargetRequiredPermissionsProvider customerTargetAccessiblePermissionsProvider(CustomerAccessPermissionCache cache) {
        return new CustomerTargetRequiredPermissionsProvider(cache);
    }

    @Bean
    public FilterRegistrationBean<Filter> customerAuthorizeFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        Filter customerAuthorizeFilter = new CustomerAuthorizeFilter();
        registration.setFilter(customerAuthorizeFilter);
        registration.addUrlPatterns("/*");
        registration.setName(CustomerAuthorizeFilter.class.getName() + "RegistrationBean");
        registration.setOrder(CustomerAuthorizeFilter.FILTER_REGISTRATION_ORDER);
        return registration;
    }
}
