package com.dn.ribbon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
@EnableEurekaClient
@EnableCircuitBreaker//客戶端的服務斷路器
//@RibbonClient(name = "helloclient", configuration = RibbonConfiguration.class)
public class RibbonDemoApplication {

    public static void main(String[] args) {

        SpringApplication.run(RibbonDemoApplication.class, args);
    }


    @Bean
    @LoadBalanced
    public RestTemplate template(){
        return new RestTemplate();
    }
}



