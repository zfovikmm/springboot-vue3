package com.zm.controller;

import com.zm.req.CategoryQueryReq;
import com.zm.req.CategorySaveReq;
import com.zm.resp.CommonResp;
import com.zm.resp.CategoryQueryResp;
import com.zm.resp.PageResp;
import com.zm.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //查询
    @GetMapping("/list")
    public CommonResp list(){
        CommonResp<List<CategoryQueryResp>> resp = new CommonResp<>();
        List<CategoryQueryResp> list = categoryService.list();
        resp.setContent(list);
        return resp;
    }

    //模糊查询
    @RequestMapping("/seLike")
    public CommonResp liselike(CategoryQueryReq req){
        CommonResp<List<CategoryQueryResp>> resp = new CommonResp<>();
        List<CategoryQueryResp> list = categoryService.list(req);
        resp.setContent(list);
        return resp;
    }

    //模糊查询+查询全部
    @RequestMapping(value = "/query",method = RequestMethod.GET)
    public CommonResp query(@Valid CategoryQueryReq req){
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        PageResp<CategoryQueryResp> list = categoryService.query(req);
        resp.setContent(list);
        System.out.println(resp);
        return resp;
    }
    //新增 编辑
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody  CategorySaveReq req){
        CommonResp resp = new CommonResp<>();
        categoryService.save(req);
        return resp;
    }

    //删除
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){
//        返回一个空的响应实体 代表成功了
        CommonResp resp = new CommonResp<>();
        categoryService.delete(id);
        return resp;
    }
}
