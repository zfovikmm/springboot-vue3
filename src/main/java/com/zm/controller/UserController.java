package com.zm.controller;

import com.zm.req.UserLoginReq;
import com.zm.req.UserQueryReq;
import com.zm.req.UserResetPasswordReq;
import com.zm.req.UserSaveReq;
import com.zm.resp.CommonResp;
import com.zm.resp.PageResp;
import com.zm.resp.UserLoginResp;
import com.zm.resp.UserQueryResp;
import com.zm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //查询
    @GetMapping("/list")
    public CommonResp list(){
        CommonResp<List<UserQueryResp>> resp = new CommonResp<>();
        List<UserQueryResp> list = userService.list();
        resp.setContent(list);
        return resp;
    }

    //模糊查询
    @RequestMapping("/seLike")
    public CommonResp liselike(UserQueryReq req){
        CommonResp<List<UserQueryResp>> resp = new CommonResp<>();
        List<UserQueryResp> list = userService.list(req);
        resp.setContent(list);
        return resp;
    }

    //模糊查询+查询全部
    @RequestMapping(value = "/query",method = RequestMethod.GET)
    public CommonResp query(@Valid UserQueryReq req){
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        PageResp<UserQueryResp> list = userService.query(req);
        resp.setContent(list);
        System.out.println(resp);
        return resp;
    }
    //新增 编辑
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody  UserSaveReq req){
        //用md5加密 密码 通过这句话就变成了一个32位的16进制的字符串
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        userService.save(req);
        return resp;
    }

    //删除
    @DeleteMapping("/delete/{idStr}")
    public CommonResp delete(@PathVariable String idStr){
//        返回一个空的响应实体 代表成功了
        CommonResp resp = new CommonResp<>();

        //将字符串转为集合
       List<String> list = Arrays.asList(idStr.split(","));
        System.out.println("idStr==========>"+idStr);
        System.out.println("list===========>"+list);
        userService.delete(list);
        return resp;
    }

    //重置密码
    @PostMapping("/reset-password")
    public CommonResp resetPassword(@Valid @RequestBody UserResetPasswordReq req){
        //用md5加密 密码 通过这句话就变成了一个32位的16进制的字符串
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        userService.resetPassword(req);
        return resp;
    }

    //登录
    @PostMapping("/login")
    public CommonResp login(@Valid @RequestBody UserLoginReq req){
        //登录进行加密 好对数据库中密码进行比较
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp<UserLoginResp> resp = new CommonResp<>();
        UserLoginResp userLoginResp = userService.login(req);
        resp.setContent(userLoginResp);
        return resp;
    }
}
