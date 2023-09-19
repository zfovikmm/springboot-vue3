package com.zm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zm.entity.Ebook;
import com.zm.mapper.EbookMapper;
import com.zm.req.EbookQueryReq;
import com.zm.req.EbookSaveReq;
import com.zm.resp.EbookQueryResp;
import com.zm.resp.PageResp;
import com.zm.util.CopyUtil;
import com.zm.util.SnowFlake;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {


    @Resource
    private EbookMapper ebookMapper;

    @Resource
    private SnowFlake snowFlake;



    //查询
    public List<EbookQueryResp> list(){
        List<Ebook> ebooklist = ebookMapper.selectList(null);
        /*
        List<EbookResp> ebookResps = new ArrayList<>();
        //将实体类的内容转换为EbookResp  在controller层不要见到实体类
        for (Ebook ebook : ebooks) {
            //EbookResp ebookResp = new EbookResp(ebook.getId(),ebook.getName(),ebook.getCategory1Id(),ebook.getCategory2Id(),ebook.getDescription(),ebook.getCover(),ebook.getDocCount(),ebook.getViewCount(),ebook.getVoteCount());
            EbookResp ebookResp = new EbookResp();
            BeanUtils.copyProperties(ebook,ebookResp);
            ebookResps.add(ebookResp);
        }
         */
        List<EbookQueryResp> list = CopyUtil.copyList(ebooklist, EbookQueryResp.class);
        return list;
    }

    //模糊查询
    public List<EbookQueryResp> list(EbookQueryReq req){
        //queryWrapper是mybatis plus中实现查询的对象封装操作类
        QueryWrapper<Ebook> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",req.getName());
        List<Ebook> ebooklist = ebookMapper.selectList(queryWrapper);
        //使用工具类CopyUtil 将Ebook转换为EbookResp
        List<EbookQueryResp> list = CopyUtil.copyList(ebooklist, EbookQueryResp.class);
        return list;
    }

    //模糊查询加查询全部 使用动态sql
    public PageResp<EbookQueryResp> query(EbookQueryReq req){

        QueryWrapper<Ebook> queryWrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(req.getName())){
//          queryWrapper.like（“属性”,“值”）——模糊查询匹配值‘%值%’
            queryWrapper.like("name",req.getName());
        }
//        点击导航栏时 列出电子书
        if (!ObjectUtils.isEmpty(req.getCategoryId2())){
//          queryWrapper.like（“属性”,“值”）——模糊查询匹配值‘%值%’
            queryWrapper.like("category2_id",req.getCategoryId2());
        }
        //分页查询
        PageHelper.startPage(req.getPage() ,req.getSize());
        List<Ebook> ebooklist = ebookMapper.selectList(queryWrapper);
        PageInfo<Ebook> pageInfo = new PageInfo<>(ebooklist);
        pageInfo.getTotal();        //总行数 传给前端
        //pageInfo.getPages();        //总页数

        //使用工具类CopyUtil 将Ebook转换为EbookResp
        List<EbookQueryResp> list = CopyUtil.copyList(ebooklist, EbookQueryResp.class);
        PageResp<EbookQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

//    保存
    public void save(EbookSaveReq req){
        Ebook ebook = CopyUtil.copy(req,Ebook.class);
        if(req.getId() == 0){
            //新增
            ebook.setId(snowFlake.nextId());  //雪花算法生成id
            ebookMapper.insert(ebook);
        }else {
            //保存
            ebookMapper.updateById(ebook);
        }
    }

//    删除
    public void delete(Long id ){
        ebookMapper.deleteById(id);
    }
}
