package com.zm.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EbookQueryResp {

  @JsonSerialize(using = ToStringSerializer.class)
  private long id;
  private String name;
  private long category1Id;
  private long category2Id;
  private String description;
  private String cover;
  private long docCount;
  private long viewCount;
  private long voteCount;

}
