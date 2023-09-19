package com.zm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zm.entity.Content;
import com.zm.entity.User;
import com.zm.mapper.ContentMapper;
import com.zm.mapper.UserMapper;
import com.zm.req.UserQueryReq;
import com.zm.req.UserSaveReq;
import com.zm.resp.PageResp;
import com.zm.resp.UserQueryResp;
import com.zm.util.CopyUtil;
import com.zm.util.SnowFlake;
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
public class UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private ContentMapper contentMapper;
    //查询
    public List<UserQueryResp> list(){
        List<User> userlist = userMapper.selectList(null);
        /*
        List<UserResp> userResps = new ArrayList<>();
        //将实体类的内容转换为UserResp  在controller层不要见到实体类
        for (User user : users) {
            //UserResp userResp = new UserResp(user.getId(),user.getName(),user.getUser1Id(),user.getUser2Id(),user.getDescription(),user.getCover(),user.getUserCount(),user.getViewCount(),user.getVoteCount());
            UserResp userResp = new UserResp();
            BeanUtils.copyProperties(user,userResp);
            userResps.add(userResp);
        }
         */
        List<UserQueryResp> list = CopyUtil.copyList(userlist, UserQueryResp.class);
        return list;
    }

    //模糊查询
    public List<UserQueryResp> list(UserQueryReq req){
        //queryWrapper是mybatis plus中实现查询的对象封装操作类
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.like("name",req.getName());
        List<User> userlist = userMapper.selectList(queryWrapper);
        //使用工具类CopyUtil 将User转换为UserResp
        List<UserQueryResp> list = CopyUtil.copyList(userlist, UserQueryResp.class);
        return list;
    }

    //模糊查询加查询全部 使用动态sql
    public PageResp<UserQueryResp> query(UserQueryReq req){

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(req.getLoginName())){
//          queryWrapper.like（“属性”,“值”）——模糊查询匹配值‘%值%’
            queryWrapper.like("name",req.getLoginName());
        }
        //分页查询
        PageHelper.startPage(req.getPage() ,req.getSize());
        List<User> userlist = userMapper.selectList(queryWrapper);
        PageInfo<User> pageInfo = new PageInfo<>(userlist);
        pageInfo.getTotal();        //总行数 传给前端
        //pageInfo.getPages();        //总页数



        //使用工具类CopyUtil 将User转换为UserResp
        List<UserQueryResp> list = CopyUtil.copyList(userlist, UserQueryResp.class);
        System.out.println(list);
        PageResp<UserQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        System.out.println(pageResp);
        pageResp.setList(list);
        System.out.println(pageResp);
        return pageResp;
    }

    //    保存
    public void save(UserSaveReq req){
        User user = CopyUtil.copy(req,User.class);
        Content content = CopyUtil.copy(req,Content.class);
        System.out.println("content==========>"+content);
        if(req.getId() == 0){
            //新增
            user.setId(snowFlake.nextId());  //雪花算法生成id
            userMapper.insert(user);

            //新增 内容
            content.setId(user.getId());  //雪花算法生成id
            contentMapper.insert(content);
        }else {
            //更新
            userMapper.updateById(user);
            contentMapper.updateById(content);
        }
    }

    //    删除
    public void delete(Long id ){
        userMapper.deleteById(id);
    }

    //批量删除
    public void delete(List<String> ids ){
        System.out.println("ids===========>"+ids);
        userMapper.deleteBatchIds(ids);
    }
}
