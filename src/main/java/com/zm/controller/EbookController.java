package com.zm.controller;

import com.zm.req.EbookQueryReq;
import com.zm.req.EbookSaveReq;
import com.zm.resp.CommonResp;
import com.zm.resp.EbookQueryResp;
import com.zm.resp.PageResp;
import com.zm.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class EbookController {

    @Autowired
    private EbookService ebookService;

    //查询
    @GetMapping("/list")
    public CommonResp list(){
        CommonResp<List<EbookQueryResp>> resp = new CommonResp<>();
        List<EbookQueryResp> list = ebookService.list();
        resp.setContent(list);
        return resp;
    }

    //模糊查询
    @RequestMapping("/seLike")
    public CommonResp liselike(EbookQueryReq req){
        CommonResp<List<EbookQueryResp>> resp = new CommonResp<>();
        List<EbookQueryResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }

    //模糊查询+查询全部
    @RequestMapping(value = "/query",method = RequestMethod.GET)
    public CommonResp query(@Valid EbookQueryReq req){
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.query(req);
        resp.setContent(list);
        System.out.println(resp);
        return resp;
    }

    //新增 编辑
    @PostMapping("/save")
    public CommonResp save(@RequestBody EbookSaveReq req){
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }

    //删除
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){
//        返回一个空的响应实体 代表成功了
        CommonResp resp = new CommonResp<>();
        ebookService.delete(id);
        return resp;
    }
}
