package com.qdch.yct.web.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author sniper
 */
@Configuration
public class DataSourceConfig {
    @Autowired
    JdbcConfig jdbcConfig;

    @Bean
    public DataSource druidDataSource() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setPoolPreparedStatements(false);
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setUseUnfairLock(true);
        druidDataSource.setFilters("stat");

        druidDataSource.setUrl(jdbcConfig.getUrl());
        druidDataSource.setUsername(jdbcConfig.getUserName());
        druidDataSource.setPassword(jdbcConfig.getPassWord());
        druidDataSource.setDriverClassName(jdbcConfig.getDriverClassName());
        druidDataSource.setInitialSize(jdbcConfig.getInitialSize());
        druidDataSource.setMaxActive(jdbcConfig.getMaxActive());
        druidDataSource.setMinIdle(jdbcConfig.getMinIdle());
        druidDataSource.setMaxWait(jdbcConfig.getMaxWaitTime());
        druidDataSource.setTimeBetweenEvictionRunsMillis(jdbcConfig
                .getTimeBetweenEvictionRunsMillis());
        druidDataSource.setMinEvictableIdleTimeMillis(jdbcConfig.getMinEvictableIdleTimeMillis());
        druidDataSource.setValidationQuery(jdbcConfig.getValidationQuery());

        druidDataSource.setRemoveAbandoned(true);
        druidDataSource.setRemoveAbandonedTimeout(1800);

        return druidDataSource;
    }
}
