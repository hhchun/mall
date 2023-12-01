package com.hhchun.mall.access.config;

import com.hhchun.mall.access.support.annotation.EnableAccessControl;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.hhchun.mall.access"})
@EnableAccessControl
public class MallAccessCoreConfiguration {

}