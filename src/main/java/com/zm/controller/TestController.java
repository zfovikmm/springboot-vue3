package com.zm.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    /*
    常见的http请求方式：GET POST PUT DELETE
                     查询 新增  修改 删除
       传统 /user?id=1
       resultfu /user/1
       @RequestMapping 表示这个接口支持所有的访问方式
    @GetMapping
    @PutMapping
    @DeleteMapping
    @PostMapping
    @RequestMapping(value = "",method = RequestMethod.DELETE)
    RequestMethod.DELETE HEAD PUT POST OPTTONS PATCH TRACE
     */
    @RequestMapping("/hello")
    //@RequestMapping("/hello")
    public String hello(){
        return "hello world123!";
    }

    @PostMapping("/test")
    public String helloTest(String name){
        return "hello===="+name;
    }



}
