package com.zm.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserQueryResp {

  @JsonSerialize(using = ToStringSerializer.class)
  private long id;
  private String loginName;
  private String name;
  private String password;

}
