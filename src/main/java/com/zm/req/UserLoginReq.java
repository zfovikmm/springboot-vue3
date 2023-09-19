package com.zm.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginReq {

  @NotNull(message = "【名称】不能为空")
  private String loginName;
  @NotNull(message = "【密码】不能为空")
  //@Length(min = 6,max=20,message ="【密码】6~20位" )
  @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$", message = "【密码】规则不正确")
  private String password;
}
