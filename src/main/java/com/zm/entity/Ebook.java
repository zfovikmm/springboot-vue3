package com.zm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ebook {

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
