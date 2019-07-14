package com.dn.feigndemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients //启用feign，如果说需要启动默认配置，就这么写，如果需要覆盖就写成(@EnableFeignClients(defaultConfiguration = "FooConfiguartion.class"))
public class FeignDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignDemoApplication.class, args);
    }

}

