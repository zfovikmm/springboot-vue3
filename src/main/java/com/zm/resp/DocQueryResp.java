package com.zm.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocQueryResp {

  private long id;
  private long ebookId;
  private long parent;
  private String name;
  private long sort;
  private long viewCount;
  private long voteCount;

}
