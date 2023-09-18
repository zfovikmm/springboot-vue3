package com.zm.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EbookQueryReq extends PageReq {

  private long id;
  private long categoryId2;
  private String name;

}
