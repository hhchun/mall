package com.hhchun.mall.bootstrap;

import com.hhchun.mall.user.config.MallUserCoreConfiguration;
import com.hhchun.mall.user.support.decision.AccessDecision;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(MallUserCoreConfiguration.class)
public class MallBootstrapApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MallBootstrapApplication.class);
        AccessDecision accessDecision = context.getBean(AccessDecision.class);
        System.out.println(accessDecision);
    }
}
