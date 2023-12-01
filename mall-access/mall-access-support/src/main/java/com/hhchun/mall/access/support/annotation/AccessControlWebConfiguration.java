package com.hhchun.mall.access.support.annotation;


import com.hhchun.mall.access.support.decision.AccessDecision;
import com.hhchun.mall.access.support.decision.AffirmativeAccessDecision;
import com.hhchun.mall.access.support.denied.AccessDenied;
import com.hhchun.mall.access.support.denied.DefaultAccessDenied;
import com.hhchun.mall.access.support.filter.AccessControlFilter;
import com.hhchun.mall.access.support.provider.SubjectOwnedPermissionsProvider;
import com.hhchun.mall.access.support.provider.DelegatingSubjectOwnedPermissionsProvider;
import com.hhchun.mall.access.support.provider.DelegatingTargetAccessiblePermissionsProvider;
import com.hhchun.mall.access.support.provider.TargetRequiredPermissionsProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.Filter;
import java.util.Set;


/**
 * 适用于springmvc环境
 */
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(
        type = ConditionalOnWebApplication.Type.SERVLET
)
@ConditionalOnClass(DispatcherServlet.class)
@AutoConfigureAfter({DispatcherServletAutoConfiguration.class})
public class AccessControlWebConfiguration {

    @Bean
    public FilterRegistrationBean<Filter> accessControlFilterRegistration(AccessDecision accessDecision, AccessDenied accessDenied) {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        AccessControlFilter accessControlFilter = new AccessControlFilter(accessDecision, accessDenied);
        registration.setFilter(accessControlFilter);
        registration.addUrlPatterns("/*");
        registration.setName(AccessControlFilter.class.getName() + "RegistrationBean");
        registration.setOrder(AccessControlFilter.FILTER_REGISTRATION_ORDER);
        return registration;
    }

    @Bean
    @ConditionalOnMissingBean(AccessDecision.class)
    public AccessDecision accessDecision(@Qualifier(DelegatingSubjectOwnedPermissionsProvider.BEAN_NAME) SubjectOwnedPermissionsProvider sop,
                                         @Qualifier(DelegatingTargetAccessiblePermissionsProvider.BEAN_NAME) TargetRequiredPermissionsProvider trs) {
        return new AffirmativeAccessDecision(sop, trs);
    }

    @Bean
    @ConditionalOnMissingBean(AccessDenied.class)
    public AccessDenied accessDenied(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        return new DefaultAccessDenied(resolver);
    }

    @Bean(name = DelegatingSubjectOwnedPermissionsProvider.BEAN_NAME)
    @Lazy
    public SubjectOwnedPermissionsProvider delegatingSubjectOwnedPermissionsProvider(Set<SubjectOwnedPermissionsProvider> sops) {
        return new DelegatingSubjectOwnedPermissionsProvider(sops);
    }

    @Bean(name = DelegatingTargetAccessiblePermissionsProvider.BEAN_NAME)
    @Lazy
    public TargetRequiredPermissionsProvider delegatingTargetAccessiblePermissionsProvider(Set<TargetRequiredPermissionsProvider> trs) {
        return new DelegatingTargetAccessiblePermissionsProvider(trs);
    }
}
