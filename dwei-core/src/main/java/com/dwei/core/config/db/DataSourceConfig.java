package com.dwei.core.config.db;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Configuration
public class DataSourceConfig {

    @Primary
    @Bean(name = "masterDs")
    @ConfigurationProperties("spring.datasource.druid.master")
    public DataSource masterDs(DruidProperties druidProperties) {
        var dataSource = DruidDataSourceBuilder.create().build();
        log.info("master数据库连接, url:[{}], username:[{}]", dataSource.getUrl(), dataSource.getUsername());
        return druidProperties.dataSource(dataSource);
    }

}
