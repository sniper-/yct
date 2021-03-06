package com.qdch.yct.web.controller;

import com.qdch.yct.web.entity.User;
import com.qdch.yct.web.mapper.UserMapper;
import com.qdch.yct.web.pojo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

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
public class HelloController {
    @Autowired
    HttpUtil httpUtil;
    @Autowired
    UserMapper userMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/api/login")
    @CrossOrigin
    public Result login(@RequestBody User user){
        System.out.println(user.getUserName());

        // 对 html 标签进行转义，防止 XSS 攻击
        String username = HtmlUtils.htmlEscape(user.getUserName());
        System.out.println("login Controller in.");
        System.out.println(username);

        User dbUser = userMapper.selectByPrimaryKey(username);
        System.out.println(dbUser.getPassword());
        if (dbUser.getPassword().equals(user.getPassword())) {
            System.out.println("success");
            return new Result(200);
        } else {
            String message = "账号密码错误";
            System.out.println("failed");
            return new Result(400);
        }
    }

    @GetMapping(value = "/logout")
    @ResponseBody
    public ResponseEntity<Object> logout() {
        System.out.println("logout success");
        return new ResponseEntity<>("Product is created successfully", HttpStatus.OK);
    }

    @RequestMapping(value="/api/hello",produces = {"application/json;charset=UTF-8"},method={RequestMethod.POST})
    public String doPost(@RequestBody String body){
//        CommonUtil.writeLog(logger, "市场账户请求内容", body);
        System.out.println("请求内容："+body);
        return "{\"markId\":\"054\",\"bankProId\":\"B005\"}";
    }

}
