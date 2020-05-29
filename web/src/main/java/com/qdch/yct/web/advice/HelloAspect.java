package com.qdch.yct.web.advice;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * ClassName: HelloAspect
 * Version:
 * Description:
 *
 * @Program:
 * @Author: sniper
 * @Date: 2020/05/26 17:02
 */
@Aspect
//@Order(0) // 控制多个Aspect的执行顺序，越小越先执行
@Component
public class HelloAspect {
    @Pointcut("execution(public * com.qdch.yct.web.controller.HelloController.doPost(..))")
    public void pointCut() {}

    @Before("pointCut()")
    public void before(JoinPoint joinPoint) {
        System.out.println("MyAspect before ...");
        String method = joinPoint.getSignature().getName();
        System.out.println("MyAspect before Method：{"+joinPoint.getSignature().getDeclaringTypeName()+"}::{"+method+"}");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        System.out.println("ClientIP：{"+ request.getRemoteAddr()+"}");

    }

    @After("pointCut()")
    public void after() {
        System.out.println("MyAspect after ...");
    }

    @AfterReturning("pointCut()")
    public void afterReturning() {
        System.out.println("MyAspect after returning ...");
    }

    @AfterThrowing("pointCut()")
    public void afterThrowing() {
        System.out.println("MyAspect after throwing ...");
    }

    @Around("pointCut()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("MyAspect around before ...");
        joinPoint.proceed();
        System.out.println("MyAspect around after ...");
    }
}
