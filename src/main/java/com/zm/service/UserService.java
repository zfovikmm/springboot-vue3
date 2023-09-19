package com.zm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zm.entity.User;
import com.zm.exception.BusinessException;
import com.zm.exception.BusinessExceptionCode;
import com.zm.mapper.UserMapper;
import com.zm.req.UserLoginReq;
import com.zm.req.UserQueryReq;
import com.zm.req.UserResetPasswordReq;
import com.zm.req.UserSaveReq;
import com.zm.resp.PageResp;
import com.zm.resp.UserLoginResp;
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
        if(req.getId() == 0){

            User userDB = selectByLoginName(req.getLoginName());
            if(ObjectUtils.isEmpty(userDB)){
                //新增
                user.setId(snowFlake.nextId());  //雪花算法生成id
                userMapper.insert(user);
            }else {
                //用户名已存在
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }

        }else {
            //更新
            user.setPassword(null);
            userMapper.updateById(user);
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

    //校验用户名不能重复
    public User selectByLoginName(String LoginName){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("login_name",LoginName);
        List<User> userList = userMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(userList)){
            return null;
        }else {
           return userList.get(0);
        }
    }

    //重置密码
    public void resetPassword(UserResetPasswordReq req){
        User user = CopyUtil.copy(req,User.class);
        userMapper.updateById(user);
    }
    //登录
    /*
    思考：要验证用户名密码，是按用户名+密码去数据库查，还是只按用户名去数据库查
    这里，应该只按用户名去查
    这样的话，服务端就不知道是用户名错还是密码错了，知道是哪个错是有意义的
    如果大量出现用户名错，有可能黑客拿了一堆用户名在那试，如果是大量密码错，有可能黑客想破解具体某个用户
     */
    public UserLoginResp login(UserLoginReq req){
        User userDB = selectByLoginName(req.getLoginName());
        if(ObjectUtils.isEmpty(userDB)){
            //用户名不存在

            //日志输出
            //  LOG.info("用户名不存在, {}", req.getLoginName());

            //抛出异常
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        }else {
            if(userDB.getPassword().equals(req.getPassword())){
                //登录成功
                UserLoginResp userLoginResp = CopyUtil.copy(userDB,UserLoginResp.class);
                return userLoginResp;
            }else {
                //密码错误
                // 密码不对
                //LOG.info("密码不对, 输入密码：{}, 数据库密码：{}", req.getPassword(), userDb.getPassword());
                //抛出异常
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }
    }
}
