package com.qdch.yct.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import qs.common.SecurityASE;

@Component
public class JdbcConfig {
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String userName;
    @Value("${jdbc.password}")
    private String passWord;
    @Value("${jdbc.driverClassName}")
    private String driverClassName;
    @Value("${jdbc.pool.initialSize}")
    private Integer initialSize;
    @Value("${jdbc.pool.maxActive}")
    private Integer maxActive;
    @Value("${jdbc.pool.minIdle}")
    private Integer minIdle;
    @Value("${jdbc.connection.maxWaitTime}")
    private Long maxWaitTime;
    @Value("${jdbc.connection.timeBetweenEvictionRunsMillis}")
    private Long timeBetweenEvictionRunsMillis;
    @Value("${jdbc.connection.minEvictableIdleTimeMillis}")
    private Long minEvictableIdleTimeMillis;
    @Value("${jdbc.connection.maxPoolPreparedStatementPerConnectionSize}")
    private Integer maxPoolPreparedStatementPerConnectionSize;
    @Value("${jdbc.connection.validationQuery}")
    private String validationQuery;

    public String getValidationQuery() {
        return validationQuery;
    }

    public Integer getMaxPoolPreparedStatementPerConnectionSize() {
        return maxPoolPreparedStatementPerConnectionSize;
    }

    public Long getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public Long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public Long getMaxWaitTime() {
        return maxWaitTime;
    }

    public Integer getInitialSize() {
        return initialSize;
    }

    public Integer getMaxActive() {
        return maxActive;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

//    public String getUrl() {
//        try {
//            String result = SecurityASE.decode(url);
//            return result;
//        } catch (Exception e) {
//            return "";
//        }
//    }
//
//    public String getUserName() {
//        try {
//            String result = SecurityASE.decode(userName);
//            return result;
//        } catch (Exception e) {
//            return "";
//        }
//    }
//
//    public String getPassWord() {
//        try {
//            String result = SecurityASE.decode(passWord);
//            return result;
//        } catch (Exception e) {
//            return  "";
//        }
//    }

    public String getUrl() {
        return url;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }
}
