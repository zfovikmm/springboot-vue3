package com.zm.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageReq {

  @NotNull(message = "【页码】不能为空")
  private Integer page;  //页码

  @NotNull(message = "【页码】不能为空")
  @Max(value = 100,message = "【每页条数】不能超过100")
  private Integer size;  //条数

}
