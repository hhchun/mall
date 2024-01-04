package com.hhchun.mall.user.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.hhchun.mall.user.fingerprint.FingerprintCollectionFilter;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.time.LocalDateTime;

@Configuration
@ComponentScan(basePackages = {"com.hhchun.mall.user"})
public class MallUserCoreConfiguration {

    @Bean
    @ConditionalOnMissingBean(MetaObjectHandler.class)
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(MybatisPlusInterceptor.class)
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }


    @Bean
    @ConditionalOnMissingBean(FingerprintCollectionFilter.class)
    public FilterRegistrationBean<Filter> fingerprintCollectionFilterRegistrationBean() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        Filter filter = new FingerprintCollectionFilter();
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setName(FingerprintCollectionFilter.class.getName() + "RegistrationBean");
        registration.setOrder(FingerprintCollectionFilter.FILTER_REGISTRATION_ORDER);
        return registration;
    }
}