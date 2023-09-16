package com.zm.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResp<T> {
  private Long total;       //可能是int类型

  private List<T> list;        //列表数据 类型不确定

}
