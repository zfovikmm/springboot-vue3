package com.zm.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResp  {

  private long id;

  private String loginName;

  private String name;

  private String token;

}
