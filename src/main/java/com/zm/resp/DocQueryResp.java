package com.zm.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocQueryResp {

  @JsonSerialize(using = ToStringSerializer.class)
  private long id;
  private long ebookId;
  private long parent;
  private String name;
  private long sort;
  private long viewCount;
  private long voteCount;

}
