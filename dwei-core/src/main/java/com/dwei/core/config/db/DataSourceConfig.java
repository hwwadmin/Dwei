package com.dwei.core.config.db;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 数据源配置
 *
 * @author hww
 */
@Configuration
public class DataSourceConfig {

    @Primary
    @Bean(name = "masterDs")
    @ConfigurationProperties("spring.datasource.druid.master")
    public DataSource masterDs(DruidProperties druidProperties) {
        return druidProperties.mixin(DruidDataSourceBuilder.create().build());
    }

}
