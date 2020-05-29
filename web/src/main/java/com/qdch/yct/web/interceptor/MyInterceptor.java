package com.qdch.yct.web.interceptor;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ClassName: MyInterceptor
 * Version:
 * Description:
 *
 * @Program:
 * @Author: sniper
 * @Date: 2020/05/26 09:13
 */
@Component
public class MyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        System.out.println("Pre Handle method is Calling【"+handler.getClass()+"】");
        RequestWrapper myRequestWrapper = new RequestWrapper((HttpServletRequest) request);
        String body = myRequestWrapper.getBody();
        System.out.println("我是拦截器："+body);

        String reqUrl = request.getRequestURL().toString();//得到请求的URL地址
        String reqUri = request.getRequestURI();//得到请求的资源
        String queryString = request.getQueryString();//得到请求的URL地址中附带的参数
        String remoteAddr = request.getRemoteAddr();//得到来访者的IP地址
        String remoteHost = request.getRemoteHost();
        int remotePort = request.getRemotePort();
        String remoteUser = request.getRemoteUser();
        String method = request.getMethod();//得到请求URL地址时使用的方法
        String pathInfo = request.getPathInfo();
        String localAddr = request.getLocalAddr();//获取WEB服务器的IP地址
        String localName = request.getLocalName();//获取WEB服务器的主机名

        System.out.println("获取到的客户机信息如下：");
        System.out.println("请求的URL地址："+reqUrl);
        System.out.println("请求的资源："+reqUri);
        System.out.println("请求的URL地址中附带的参数："+queryString);
        System.out.println("来访者的IP地址："+remoteAddr);
        System.out.println("来访者的主机名："+remoteHost);
        System.out.println("使用的端口号："+remotePort);
        System.out.println("remoteUser："+remoteUser);
        System.out.println("请求使用的方法："+method);
        System.out.println("pathInfo："+pathInfo);
        System.out.println("localAddr："+localAddr);
        System.out.println("localName："+localName);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        System.out.println("Post Handle method is Calling");
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        System.out.println("Request and Response is completed");
    }
}
