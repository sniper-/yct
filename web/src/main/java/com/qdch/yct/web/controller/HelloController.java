package com.qdch.yct.web.controller;

import com.qdch.yct.web.pojo.Result;
import com.qdch.yct.web.pojo.User;
import com.qdch.yct.web.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.Objects;

/**
 * ClassName: HelloController
 * Version:
 * Description:
 *
 * @Program:
 * @Author: sniper
 * @Date: 2020/05/19 13:14
 */
@RestController
@RequestMapping("/api")
public class HelloController {
    @Autowired
    HttpUtil httpUtil;

    @PostMapping(value = "/login")
    @ResponseBody
    public ResponseEntity<Object> login(){
        System.out.println("test in");
        return new ResponseEntity<>("Product is created successfully", HttpStatus.OK);


        // 对 html 标签进行转义，防止 XSS 攻击
//        String username = requestUser.getUsername();
//        username = HtmlUtils.htmlEscape(username);
//
//        if (!Objects.equals("admin", username) || !Objects.equals("123456", requestUser.getPassword())) {
//            String message = "账号密码错误";
//            System.out.println("failed");
//            return new Result(400);
//        } else {
//            System.out.println("success");
//            return new Result(200);
//        }
    }


    @GetMapping(value = "/logout")
    @ResponseBody
    public ResponseEntity<Object> logout() {
        System.out.println("logout success");
        return new ResponseEntity<>("Product is created successfully", HttpStatus.OK);
    }
    @RequestMapping(method = {RequestMethod.POST,
            RequestMethod.GET},produces = {"application/json;charset=UTF-8"})
    public String doPost(@RequestBody String body){
        return "{\"markId\":\"054\",\"bankProId\":\"B005\"}";
    }

}
