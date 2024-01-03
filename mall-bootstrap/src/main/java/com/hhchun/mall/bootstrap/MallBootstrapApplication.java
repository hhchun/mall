package com.hhchun.mall.bootstrap;

import com.hhchun.mall.user.config.MallUserCoreConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(MallUserCoreConfiguration.class)
public class MallBootstrapApplication {
    public static void main(String[] args) {
       SpringApplication.run(MallBootstrapApplication.class);
    }
}
