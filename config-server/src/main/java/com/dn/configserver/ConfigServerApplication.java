package com.dn.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@EnableConfigServer
@EnableEurekaClient
public class ConfigServerApplication {

    public static void main(String[] args) {

        /**
         * spring 内部事件演示
         * */
        //创建spring 基于注解驱动的应用上下文
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        //注册到sping上下文中
        context.register(EventConfiguration.class);
        //启动spring上下文
        context.refresh();

        ApplicationEventPublisher publisher = context;

        //发布一个事件
        publisher.publishEvent(new MyApplicationEvent("dn的nick老师发布了一个事件"));
//


        SpringApplication.run(ConfigServerApplication.class, args);
//        System.out.println(System.getProperty("user.dir"));
    }


    //自定义实现一个事件发布
    public static class MyApplicationEvent extends ApplicationEvent {
        public MyApplicationEvent(String msg) {
            super(msg);
        }
    }

    @Configuration
    public static class EventConfiguration {

        /**
         * 监听 {@link MyApplicationEvent}
         *
         * @param event {@MyApplicationEvent}
         */
        @EventListener
        public void onEvent(MyApplicationEvent event) {
            System.out.println("监听到事件：" + event);
            //各种实现
        }
    }

    }
