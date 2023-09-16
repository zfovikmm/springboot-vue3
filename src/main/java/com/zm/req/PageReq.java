package com.zm.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageReq {
  private Integer page;  //页码
  private Integer size;  //条数

}
