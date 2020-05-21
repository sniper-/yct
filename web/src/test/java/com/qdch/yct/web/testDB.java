package com.qdch.yct.web;


import com.qdch.yct.web.config.JdbcConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * ClassName: testDB
 * Version:
 * Description:
 *
 * @Program:
 * @Author: sniper
 * @Date: 2020/05/21 10:54
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WebApplication.class)
public class testDB {
    @Autowired
    JdbcConfig jdbcConfig;
    @Test
    public void testDB1(){
        System.out.println(jdbcConfig.getUrl());
        System.out.println(jdbcConfig.getUserName());
        System.out.println(jdbcConfig.getPassWord());
    }

}
