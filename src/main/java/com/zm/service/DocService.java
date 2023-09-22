package com.zm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zm.entity.Content;
import com.zm.entity.Doc;
import com.zm.exception.BusinessException;
import com.zm.exception.BusinessExceptionCode;
import com.zm.mapper.ContentMapper;
import com.zm.mapper.DocMapper;
import com.zm.mapper.MyDocMapper;
import com.zm.req.DocQueryReq;
import com.zm.req.DocSaveReq;
import com.zm.resp.DocQueryResp;
import com.zm.resp.PageResp;
import com.zm.util.CopyUtil;
import com.zm.util.RedisUtil;
import com.zm.util.RequestContext;
import com.zm.util.SnowFlake;
import com.zm.websocket.WebSocketServer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 分类 服务类
 * </p>
 *
 * @author glimpse
 * @since 2023-09-17
 */
@Service
public class DocService {
    @Resource
    private DocMapper docMapper;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private MyDocMapper myDocMapper;

    @Resource
    public RedisUtil redisUtil;

    @Resource
    private WebSocketServer webSocketServer;

    //查询
    public List<DocQueryResp> list(){
        List<Doc> doclist = docMapper.selectList(null);
        /*
        List<DocResp> docResps = new ArrayList<>();
        //将实体类的内容转换为DocResp  在controller层不要见到实体类
        for (Doc doc : docs) {
            //DocResp docResp = new DocResp(doc.getId(),doc.getName(),doc.getDoc1Id(),doc.getDoc2Id(),doc.getDescription(),doc.getCover(),doc.getDocCount(),doc.getViewCount(),doc.getVoteCount());
            DocResp docResp = new DocResp();
            BeanUtils.copyProperties(doc,docResp);
            docResps.add(docResp);
        }
         */
        List<DocQueryResp> list = CopyUtil.copyList(doclist, DocQueryResp.class);
        return list;
    }

    //模糊查询
    public List<DocQueryResp> list(DocQueryReq req){
        //queryWrapper是mybatis plus中实现查询的对象封装操作类
        QueryWrapper<Doc> queryWrapper = new QueryWrapper<>();
//        queryWrapper.like("name",req.getName());
        List<Doc> doclist = docMapper.selectList(queryWrapper);
        //使用工具类CopyUtil 将Doc转换为DocResp
        List<DocQueryResp> list = CopyUtil.copyList(doclist, DocQueryResp.class);
        return list;
    }

    //模糊查询加查询全部 使用动态sql
    public PageResp<DocQueryResp> query(DocQueryReq req){

        QueryWrapper<Doc> queryWrapper = new QueryWrapper<>();
//        if (!ObjectUtils.isEmpty(req.getName())){
////          queryWrapper.like（“属性”,“值”）——模糊查询匹配值‘%值%’
//            queryWrapper.like("name",req.getName());
//        }
        //分页查询
        PageHelper.startPage(req.getPage() ,req.getSize());
        List<Doc> doclist = docMapper.selectList(queryWrapper);
        PageInfo<Doc> pageInfo = new PageInfo<>(doclist);
        pageInfo.getTotal();        //总行数 传给前端
        //pageInfo.getPages();        //总页数



        //使用工具类CopyUtil 将Doc转换为DocResp
        List<DocQueryResp> list = CopyUtil.copyList(doclist, DocQueryResp.class);
        System.out.println(list);
        PageResp<DocQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        System.out.println(pageResp);
        pageResp.setList(list);
        System.out.println(pageResp);
        return pageResp;
    }

    //    保存
    public void save(DocSaveReq req){
        Doc doc = CopyUtil.copy(req,Doc.class);
        Content content = CopyUtil.copy(req,Content.class);
        System.out.println("content==========>"+content);
        if(req.getId() == 0){
            //新增
            doc.setId(snowFlake.nextId());  //雪花算法生成id
            docMapper.insert(doc);

            //新增 内容
            content.setId(doc.getId());  //雪花算法生成id
            contentMapper.insert(content);
        }else {
            //更新
            docMapper.updateById(doc);
            contentMapper.updateById(content);
        }
    }

    //    删除
    public void delete(Long id ){
        docMapper.deleteById(id);
    }

    //批量删除
    public void delete(List<String> ids ){
        System.out.println("ids===========>"+ids);
        docMapper.deleteBatchIds(ids);
    }

    //根据id进行查找文档
    public String findContent(long id ){
        Content content = contentMapper.selectById(id);
        //文档阅读数+1
        myDocMapper.increaseViewCount(id);
        if (ObjectUtils.isEmpty(content)){
            return "";
        }else {
            return content.getContent();
        }
    }

//    public List<DocQueryResp> all(long ebookId){
//        QueryWrapper<Doc> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("ebook_id",ebookId);
//        queryWrapper.orderByAsc("sort");
//
//        List<Doc> list = this.list(queryWrapper);
//        Doc doc = docMapper.selectById(ebookId);
//        docMapper.select(doc.getId())
//        DocQueryResp docQueryResp = CopyUtil.copy(docs, DocQueryResp.class);
//        return list;
//    }

    //点赞
    public void vote(long id){
        // myDocMapper.increaseVoteCount(id);  原本只有点赞功能
        // 远程IP+doc.id作为key，24小时内不能重复
        String ip = RequestContext.getRemoteAddr();
        if (redisUtil.validateRepeat("DOC_VOTE_" + id + "_" + ip, 3600 * 24)) {
            myDocMapper.increaseVoteCount(id);
        } else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }

        Doc docDb = docMapper.selectById(id);
        //推送消息
        webSocketServer.sendInfo("【"+docDb.getName()+"】被点赞了！");

    }

    //更新
    public void updateEbookInfo(){
        //更新电子书下的文档数据开始
        myDocMapper.updateEbookInfo();
    }
}
