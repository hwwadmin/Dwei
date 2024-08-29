package com.dwei.core.config.db;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 数据源默认配置
 *
 * @author hww
 */
@Slf4j
@Configuration
public class DefaultDataSource {

    @Primary
    @Bean(name = "masterDataSource")
    @ConfigurationProperties("spring.datasource.druid")
    public DataSource masterDataSource() {
        log.info("开始创建主数据源");
        return DruidDataSourceBuilder.create().build();
    }

}
