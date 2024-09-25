//package com.dwei.core.config.db;
//
//import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//import javax.sql.DataSource;
//
///**
// * 数据源配置
// *
// * @author hww
// */
//@Configuration
//@Slf4j
//public class DataSourceConfig {
//
//    public static final String MASTER_DS = "masterDs";
//    public static final String SLAVE_DS = "slaveDs";
//
//    @Primary
//    @Bean(name = MASTER_DS)
//    @ConfigurationProperties("spring.datasource.druid.master")
//    @ConditionalOnProperty(prefix = "spring.datasource.druid.master", name = "url")
//    public DataSource masterDs(DruidProperties druidProperties) {
//        return druidProperties.mixin(DruidDataSourceBuilder.create().build());
//    }
//
//    @Bean(name = SLAVE_DS)
//    @ConfigurationProperties("spring.datasource.druid.slave")
//    @ConditionalOnProperty(prefix = "spring.datasource.druid.slave", name = "url")
//    public DataSource slaveDs(DruidProperties druidProperties) {
//        return druidProperties.mixin(DruidDataSourceBuilder.create().build());
//    }
//
//}
