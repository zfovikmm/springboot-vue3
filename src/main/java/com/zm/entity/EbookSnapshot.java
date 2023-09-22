package com.zm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EbookSnapshot {

  private long id;
  private long ebookId;
  private java.sql.Date date;
  private long viewCount;
  private long voteCount;
  private long viewIncrease;
  private long voteIncrease;

}
