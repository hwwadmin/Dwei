package com.dwei.core.config.db;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 数据源配置
 *
 * @author hww
 */
@Configuration
@Slf4j
public class DataSourceConfig {

    @Bean(name = "masterDs")
    @ConfigurationProperties("spring.datasource.dynamic.master")
    public DataSource masterDs(DruidProperties druidProperties) {
        log.info("数据源[masterDs]构建");
        return druidProperties.mixin(DruidDataSourceBuilder.create().build());
    }

    @Bean(name = "slaveDs")
    @ConfigurationProperties("spring.datasource.dynamic.slave")
    @ConditionalOnProperty(prefix = "spring.datasource.dynamic.slave", name = "enable", havingValue = "true")
    public DataSource slaveDs(DruidProperties druidProperties) {
        log.info("数据源[slaveDs]构建");
        return druidProperties.mixin(DruidDataSourceBuilder.create().build());
    }

}
