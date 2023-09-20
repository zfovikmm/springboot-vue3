package com.zm.controller;

import com.zm.req.DocQueryReq;
import com.zm.req.DocSaveReq;
import com.zm.resp.DocQueryResp;
import com.zm.resp.CommonResp;
import com.zm.resp.PageResp;
import com.zm.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {

    @Autowired
    private DocService docService;

    //查询
    @GetMapping("/list")
    public CommonResp list(){
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.list();
        resp.setContent(list);
        return resp;
    }

    //模糊查询
    @RequestMapping("/seLike")
    public CommonResp liselike(DocQueryReq req){
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.list(req);
        resp.setContent(list);
        return resp;
    }

    //模糊查询+查询全部
    @RequestMapping(value = "/query",method = RequestMethod.GET)
    public CommonResp query(@Valid DocQueryReq req){
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.query(req);
        resp.setContent(list);
        System.out.println(resp);
        return resp;
    }
    //新增 编辑
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody  DocSaveReq req){
        CommonResp resp = new CommonResp<>();
        docService.save(req);
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
        docService.delete(list);
        return resp;
    }

    //点击某一个文档的时候在去加载文档内容
    @GetMapping("/find-content/{id}")
    public CommonResp findContent(@PathVariable long id){
        CommonResp<String> resp = new CommonResp<>();
        String content = docService.findContent(id);
        resp.setContent(content);
        return resp;
    }
}
