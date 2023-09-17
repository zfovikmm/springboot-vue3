package com.zm.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EbookSaveReq {

  private long id;
  @NotNull(message = "【名称】不能为空")
  private String name;
  private long category1Id;
  private long category2Id;
  private String description;
  private String cover;
  private long docCount;
  private long viewCount;
  private long voteCount;

}
