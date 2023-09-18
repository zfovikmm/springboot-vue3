package com.zm.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocSaveReq {

  private long id;
  @NotNull(message = "【电子书】不能为空")
  private long ebookId;
  @NotNull(message = "【父文档】不能为空")
  private long parent;
  @NotNull(message = "【名称】不能为空")
  private String name;
  @NotNull(message = "【排序】不能为空")
  private long sort;
  private long viewCount;
  private long voteCount;

}
