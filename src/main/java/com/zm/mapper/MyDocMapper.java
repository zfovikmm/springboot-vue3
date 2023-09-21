package com.zm.mapper;

import org.apache.ibatis.annotations.Param;

public interface MyDocMapper {


    //浏览量
    public void increaseViewCount(@Param("id") long id );

    //点赞
    public viod increaseVoteCount(@Param("id") long id);
}
