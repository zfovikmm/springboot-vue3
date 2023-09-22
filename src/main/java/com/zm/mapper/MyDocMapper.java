package com.zm.mapper;

import org.apache.ibatis.annotations.Param;

public interface MyDocMapper {


    //浏览量
    void increaseViewCount(@Param("id") long id );

    //点赞
    void increaseVoteCount(@Param("id") long id);

    //跟新
    void updateEbookInfo();
}
