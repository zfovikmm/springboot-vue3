package com.zm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zm.entity.Category;
import com.zm.mapper.CategoryMapper;
import com.zm.req.CategoryQueryReq;
import com.zm.req.CategorySaveReq;
import com.zm.resp.CategoryQueryResp;
import com.zm.resp.PageResp;
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
public class CategoryService  {
    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;

    //查询
    public List<CategoryQueryResp> list(){
        List<Category> categorylist = categoryMapper.selectList(null);
        /*
        List<CategoryResp> categoryResps = new ArrayList<>();
        //将实体类的内容转换为CategoryResp  在controller层不要见到实体类
        for (Category category : categorys) {
            //CategoryResp categoryResp = new CategoryResp(category.getId(),category.getName(),category.getCategory1Id(),category.getCategory2Id(),category.getDescription(),category.getCover(),category.getDocCount(),category.getViewCount(),category.getVoteCount());
            CategoryResp categoryResp = new CategoryResp();
            BeanUtils.copyProperties(category,categoryResp);
            categoryResps.add(categoryResp);
        }
         */
        List<CategoryQueryResp> list = CopyUtil.copyList(categorylist, CategoryQueryResp.class);
        return list;
    }

    //模糊查询
    public List<CategoryQueryResp> list(CategoryQueryReq req){
        //queryWrapper是mybatis plus中实现查询的对象封装操作类
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
//        queryWrapper.like("name",req.getName());
        List<Category> categorylist = categoryMapper.selectList(queryWrapper);
        //使用工具类CopyUtil 将Category转换为CategoryResp
        List<CategoryQueryResp> list = CopyUtil.copyList(categorylist, CategoryQueryResp.class);
        return list;
    }

    //模糊查询加查询全部 使用动态sql
    public PageResp<CategoryQueryResp> query(CategoryQueryReq req){

        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
//        if (!ObjectUtils.isEmpty(req.getName())){
////          queryWrapper.like（“属性”,“值”）——模糊查询匹配值‘%值%’
//            queryWrapper.like("name",req.getName());
//        }
        //分页查询
        PageHelper.startPage(req.getPage() ,req.getSize());
        List<Category> categorylist = categoryMapper.selectList(queryWrapper);
        PageInfo<Category> pageInfo = new PageInfo<>(categorylist);
        pageInfo.getTotal();        //总行数 传给前端
        //pageInfo.getPages();        //总页数



        //使用工具类CopyUtil 将Category转换为CategoryResp
        List<CategoryQueryResp> list = CopyUtil.copyList(categorylist, CategoryQueryResp.class);
        System.out.println(list);
        PageResp<CategoryQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        System.out.println(pageResp);
        pageResp.setList(list);
        System.out.println(pageResp);
        return pageResp;
    }

    //    保存
    public void save(CategorySaveReq req){
        Category category = CopyUtil.copy(req,Category.class);
        System.out.println("======="+req);
        System.out.println("==========="+req.getId());
        if(req.getId() == null){
            //新增
            category.setId(snowFlake.nextId());  //雪花算法生成id
            categoryMapper.insert(category);
        }else {
            //保存
            categoryMapper.updateById(category);
        }
    }

    //    删除
    public void delete(Long id ){
        categoryMapper.deleteById(id);
    }
}
