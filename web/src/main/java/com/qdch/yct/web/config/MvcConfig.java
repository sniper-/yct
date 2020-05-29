//package com.qdch.yct.web.config;
//
//import com.qdch.yct.web.interceptor.MyInterceptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//
///**
// * ClassName: MvcConfig
// * Version:
// * Description:
// *
// * @Program:
// * @Author: sniper
// * @Date: 2020/05/26 09:18
// */
//@Configuration
//public class MvcConfig extends WebMvcConfigurationSupport {
//    @Autowired
//    private MyInterceptor myInterceptor;
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry){
//        registry.addResourceHandler("/api/**")
//                .addResourceLocations("classpath:/api/");
//        super.addResourceHandlers(registry);
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry){
//        registry.addInterceptor(myInterceptor)
//                .addPathPatterns();
////                .excludePathPatterns("/");
//        super.addInterceptors(registry);
//    }
//}
