package com.hhchun.mall.bootstrap;

import com.hhchun.mall.access.config.MallAccessCoreConfiguration;
import com.hhchun.mall.access.support.decision.AccessDecision;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(MallAccessCoreConfiguration.class)
public class MallBootstrapApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MallBootstrapApplication.class);
        AccessDecision accessDecision = context.getBean(AccessDecision.class);
        System.out.println(accessDecision);
    }
}
