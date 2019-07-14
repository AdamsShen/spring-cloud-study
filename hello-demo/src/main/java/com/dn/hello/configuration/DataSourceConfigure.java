package com.dn.hello.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfigure {

    @Bean
    @RefreshScope// 刷新配置文件
    @ConfigurationProperties(prefix="spring.datasource") // 数据源的自动配置的前缀
    public DataSource dataSource(){
        return DataSourceBuilder.create().build();
    }


}
